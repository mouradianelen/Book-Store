package com.example.bookstore.service;

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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
}
