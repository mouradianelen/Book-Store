package com.example.bookstore.service;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import com.example.bookstore.dto.BookCSV;
import com.example.bookstore.entity.Image;
import com.example.bookstore.entity.Publisher;
import com.example.bookstore.repository.AuthorRepository;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.ImageRepository;
import com.example.bookstore.repository.PublisherRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static jdk.xml.internal.SecuritySupport.getResourceAsStream;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;
    private final ImageRepository imageRepository;

    public BookService(BookRepository movieRepository, PublisherRepository publisherRepository, AuthorRepository authorRepository, ImageRepository imageRepository) {
        this.bookRepository = movieRepository;
        this.publisherRepository = publisherRepository;
        this.authorRepository = authorRepository;
        this.imageRepository = imageRepository;
    }

    @Async
    public void saveEntities(final MultipartFile file) throws IOException {
        List<BookCSV> denormalizedBooks = uploadCSV(file);
        List<Book> books = new ArrayList<>();
        List<Publisher> publishers = new ArrayList<>();
        List<Author> authors = new ArrayList<>();
        List<Image> images = new ArrayList<>();
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
    public List<BookCSV> uploadCSV(MultipartFile file) throws IOException {
        BufferedReader fileReader = new BufferedReader(new
                InputStreamReader(file.getInputStream(), "UTF-8"));

        List<BookCSV> books = new CsvToBeanBuilder<BookCSV>(fileReader)
                .withType(BookCSV.class)
                .withSeparator(';')
                .withIgnoreEmptyLine(true)
                .withSkipLines(1)
                .build()
                .parse();
        return books;
    }
    public List<BookDto> getBooksWithMoreThanOneAuthor(int pageNo, int pageSize){
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<BookDto> pagedResult = bookRepository.findBooksWithMoreThanOneAuthor(paging).map(BookDto::mapBookEntityToBookDto);
        if(pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<BookDto>();

    }

    public List<BookDto> getMostPopularBooks(){
        List<Book> books = bookRepository.findMostPopular();
        return BookDto.mapBookEntityToBookDto(books);

    }
    public byte[]  getImageByBookTitle(String title) throws IOException {
//        System.out.println(imageRepository.findImageUrl(title).getFileURL());
        String url = imageRepository.findImageUrl(title).getFileURL();
        try {
            RestTemplate restTemplate = new RestTemplate();
            byte[] responseEntity = restTemplate.getForObject(url, String.class).getBytes(StandardCharsets.UTF_8);
            if (responseEntity != null) {
                return responseEntity;
            }
        } catch (Exception e) {
        }
        return new byte[0];
    }
}
