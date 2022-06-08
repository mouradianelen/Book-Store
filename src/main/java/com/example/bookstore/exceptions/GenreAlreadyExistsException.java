package com.example.bookstore.exceptions;

public class GenreAlreadyExistsException extends RuntimeException{
    public GenreAlreadyExistsException(String name){
        super("Genre by name "+name+" already exists");
    }
}
