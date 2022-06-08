package com.example.bookstore.exceptions;

public class GenreNotFoundException extends RuntimeException{
    public GenreNotFoundException(){
        super("No such genre in our database");
    }
}

