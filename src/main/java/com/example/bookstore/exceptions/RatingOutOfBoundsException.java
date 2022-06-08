package com.example.bookstore.exceptions;

public class RatingOutOfBoundsException extends RuntimeException {
    public RatingOutOfBoundsException() {
        super("Please choose a number between 0 and 10");
    }
}
