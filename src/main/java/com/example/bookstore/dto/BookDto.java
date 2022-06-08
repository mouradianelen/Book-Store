package com.example.bookstore.dto;


import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Genre;
import com.example.bookstore.entity.Publisher;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class BookDto {

    private String ISBN;
    @NotNull
    private String title;
    private String medium_url;
    private String large_url;
    private String small_url;
    private String publisher;
    private int pageCount;
    private List<String> genre;
    @NotNull
    private List<String> authors;

    public BookDto(){

    }


    public static BookDto mapBookEntityToBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setISBN(book.getISBN());
        bookDto.setTitle(book.getTitle());
        bookDto.setMedium_url(book.getImageURLM());
        bookDto.setAuthors(book.getAuthors().stream().map(Author::getName).collect(Collectors.toList()));
        bookDto.setPublisher(book.getPublisher().getPublisherName());
        bookDto.setPageCount(book.getPageCount());
        bookDto.setGenre(book.getGenres().stream().map(Genre::getName).collect(Collectors.toList()));

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
        book.setTitle(bookDto.getTitle());
        book.setImageURLM(bookDto.getMedium_url());
        book.setImageURLS(bookDto.getSmall_url());
        book.setPageCount(bookDto.getPageCount());
        book.setPublisher(new Publisher(bookDto.getPublisher()));
        book.setISBN(bookDto.getISBN());

        return book;
    }
}
