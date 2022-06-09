package com.example.bookstore.service;

import com.example.bookstore.dto.*;
import com.example.bookstore.entity.*;
import com.example.bookstore.exceptions.*;
import com.example.bookstore.repository.*;
import com.example.bookstore.service.util.ServiceUtil;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final BookRatingRepository bookRatingRepository;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    @Value("${images.location}")
    private String basePath;


    public BookDto addBook(BookDto bookDto) {
        Book book = BookDto.mapBookDtoToBookEntity(bookDto);
        bookDto.getAuthors().forEach(author -> {
            if (authorRepository.findByName(author) == null) {
                Author author1 = new Author();
                author1.setName(author);
                author1.getBooks().add(book);
                book.getAuthors().add(author1);

            } else {
                Author author1 = authorRepository.findByName(author);
                book.getAuthors().add(author1);
                author1.getBooks().add(book);
            }

        });
        Image image = new Image();
        image.setFileURL(bookDto.getLarge_url());
        image.setStatus(Status.NOT_DOWNLOADED);
        return BookDto.mapBookEntityToBookDto(bookRepository.save(book));
    }

    public List<BookDto> getBooks(int pageNo, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<BookDto> pagedResult = bookRepository.findAll(paging).map(BookDto::mapBookEntityToBookDto);
        if (pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();
    }

    public List<BookDto> getBooksByGenre(int pageNo, int pageSize, String sortBy, String genre) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<BookDto> pagedResult = bookRepository.findBooksByGenre(genre, paging).map(BookDto::mapBookEntityToBookDto);
        if (pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();

    }

    public List<BookDto> getBooksByAuthor(int pageNo, int pageSize, String sortBy, String author) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<BookDto> pagedResult = bookRepository.findBooksByAuthor(author, paging).map(BookDto::mapBookEntityToBookDto);
        if (pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();

    }

    public BookDto getBookByTitle(String title) {
        Book book = bookRepository.findByTitle(title);
        if (book == null)
            throw new BookNotFoundException();
        return BookDto.mapBookEntityToBookDto(book);
    }

    public GenreDto createGenre(GenreDto genreDto) {
        Genre genre = GenreDto.mapDtoToGenre(genreDto);
        if (genreRepository.existsByName(genreDto.getName()))
            throw new GenreAlreadyExistsException(genreDto.getName());
        return GenreDto.mapGenreToDto(genreRepository.save(genre));
    }

    public BookRatingDto rateBook(BookRatingDto bookRatingDto) {
        Book book = bookRepository.findByTitle(bookRatingDto.getBookTitle());
        UserEntity user = userRepository.findByUsername(bookRatingDto.getUsername());
        if (book == null)
            throw new BookNotFoundException();
        if (user == null)
            throw new UserNotFoundException(bookRatingDto.getUsername());
        if (bookRatingDto.getRating() < 0 || bookRatingDto.getRating() > 10)
            throw new RatingOutOfBoundsException();

        BookRating bookRating = new BookRating();
        bookRating.setBook(book);
        bookRating.setUser(user);
        bookRating.setRating(bookRatingDto.getRating());
        return BookRatingDto.mapRatingToDto(bookRatingRepository.save(bookRating));
    }

    @Transactional
    public AssignGenreDto assignGenreToBook(AssignGenreDto assignGenreDto) {
        Book book = bookRepository.findByTitle(assignGenreDto.getTitle());
        Genre genre = genreRepository.findByName(assignGenreDto.getGenre());
        if (book == null)
            throw new BookNotFoundException();
        if (genre == null)
            throw new GenreNotFoundException();
        book.getGenres().add(genre);
        genre.getBooks().add(book);
        return assignGenreDto;
    }


    public void saveEntities(final MultipartFile file) throws IOException {
        List<BookCSV> denormalizedBooks = uploadCSV(file);
        List<CompletableFuture<Void>> callables = new ArrayList<>();
        ServiceUtil.getBatches(denormalizedBooks, 1000).forEach(books -> {
            callables.add(createTask(books));
        });

        for (CompletableFuture<Void> callable : callables) {
        }

    }

    private void processBatch(List<BookCSV> denormalizedBooks) {
        Set<Book> books = new HashSet<>();
        Set<Publisher> publishers = new HashSet<>();
        Set<Author> authors = new HashSet<>();
        Set<Image> images = new HashSet<>();
        denormalizedBooks.stream().forEach(bookCSV -> {
            Publisher publisher = new Publisher();
            Author author = new Author();
            Image image = new Image();
            author.setName(bookCSV.getAuthor());
            publisher.setPublisherName(bookCSV.getPublisher());
            Book book = new Book();
            book.setISBN(bookCSV.getBookId());
            book.setPublisher(publisher);
            book.setTitle(bookCSV.getTitle());
            book.setImageURLM(bookCSV.getImageURLM());
            book.setImageURLS(bookCSV.getImageURLS());
            book.addAuthor(author);
            author.addBook(book);
            image.setFileURL(bookCSV.getImageURLL());
            book.setImage(image);
            books.add(book);
            publishers.add(publisher);
            authors.add(author);
            images.add(image);
        });
        bookRepository.saveAll(books);
        publisherRepository.saveAll(publishers);
        authorRepository.saveAll(authors);
        imageRepository.saveAll(images);
    }

    private CompletableFuture<Void> createTask(List<BookCSV> books) {
        return CompletableFuture.supplyAsync(() -> {
            String uuid = UUID.randomUUID().toString();
            System.out.println(">>>>>>>>>>>>>>>>>>>> execution started, id: " + uuid);
            processBatch(books);
            System.out.println(">>>>>>>>>>>>>>>>>>>> execution ended, id: " + uuid);
            return null;
        }, executor);
    }

    public List<BookCSV> uploadCSV(MultipartFile file) throws IOException {
        BufferedReader fileReader = new BufferedReader(new
                InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));

        List<BookCSV> books = new CsvToBeanBuilder<BookCSV>(fileReader)
                .withType(BookCSV.class)
                .withSeparator(';')
                .withIgnoreEmptyLine(true)
                .withSkipLines(1)
                .build()
                .parse();
        return books;
    }

    public List<BookDto> getBooksWithMoreThanOneAuthor(int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<BookDto> pagedResult = bookRepository.findBooksWithMoreThanOneAuthor(paging).map(BookDto::mapBookEntityToBookDto);
        if (pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();

    }

    public List<BookDto> getMostPopularBooks() {
        List<Book> books = bookRepository.findMostPopular();
        return BookDto.mapBookEntityToBookDto(books);

    }

    public byte[] getImageByBookTitle(String title) throws IOException {

        Book book = bookRepository.findByTitle(title);
        String url = basePath + "/" + book.getImage().getId() + ".jpg";
        InputStream in = new FileInputStream(url);
        if (in == null) {
            throw new NullPointerException("The image of book with url " + url + " is not found");
        }
        return IOUtils.toByteArray(in);
    }
}
