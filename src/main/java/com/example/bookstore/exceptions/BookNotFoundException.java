package com.example.bookstore.exceptions;

public class BookNotFoundException extends RuntimeException{
    public BookNotFoundException(){
        super("No such book in our database");
    }
}
