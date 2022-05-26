package com.example.bookstore.dto;


import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Publisher;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class BookDto {

    private String ISBN;
    @NotNull
    private String title;
    private String medium_url;
    private String publisher;
    @NotNull
    private String author;

    public BookDto(){

    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMedium_url() {
        return medium_url;
    }

    public void setMedium_url(String medium_url) {
        this.medium_url = medium_url;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public static BookDto mapBookEntityToBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setISBN(book.getISBN());
        bookDto.setTitle(book.getTitle());
        bookDto.setMedium_url(book.getImageURLM());
        bookDto.setAuthor(book.getAuthors().iterator().next().getName());
        bookDto.setPublisher(book.getPublisher().getPublisherName());

        return bookDto;
    }

    public static List<BookDto> mapBookEntityToBookDto(List<Book> books) {
        return books.stream().map(BookDto::mapBookEntityToBookDto).collect(Collectors.toList());
    }
    public static List<Book> mapBookDtoToBookEntity(List<BookDto> books){
        return books.stream().map(BookDto::mapBookDtoToBookEntity).collect(Collectors.toList());
    }

    public static Book mapBookDtoToBookEntity(BookDto bookDto) {
        Book book = new Book();
        Author author = new Author();
        author.setName(bookDto.getAuthor());
        book.setTitle(bookDto.getTitle());
        book.setImageURLM(bookDto.getMedium_url());
        book.addAuthor(author);
        author.getBooks().add(book);
        book.setPublisher(new Publisher(bookDto.getPublisher()));
        book.setISBN(bookDto.getISBN());

        return book;
    }
}
