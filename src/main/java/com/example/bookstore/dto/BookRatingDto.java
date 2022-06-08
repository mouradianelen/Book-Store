package com.example.bookstore.dto;

import com.example.bookstore.entity.BookRating;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookRatingDto {
    private String bookTitle;
    private String username;
    private int rating;

    public static BookRatingDto mapRatingToDto(BookRating bookRating){
        BookRatingDto bookRatingDto = new BookRatingDto();
        bookRatingDto.setRating(bookRating.getRating());
        bookRatingDto.setUsername(bookRating.getUser().getUsername());
        bookRatingDto.setBookTitle(bookRating.getBook().getTitle());
        return  bookRatingDto;
    }
}
