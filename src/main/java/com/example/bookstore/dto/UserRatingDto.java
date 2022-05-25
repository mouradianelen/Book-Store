package com.example.bookstore.dto;

import com.opencsv.bean.CsvBindByPosition;

import javax.validation.constraints.NotNull;

public class UserRatingDto {
    @NotNull
    @CsvBindByPosition(position = 0)
    private long userId;
    @NotNull
    @CsvBindByPosition(position = 1)
    private String bookISBN;
    @NotNull
    @CsvBindByPosition(position = 2)
    private int bookRating;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getBookISBN() {
        return bookISBN;
    }

    public void setBookISBN(String bookISBN) {
        this.bookISBN = bookISBN;
    }

    public int getBookRating() {
        return bookRating;
    }

    public void setBookRating(int bookRating) {
        this.bookRating = bookRating;
    }
}
