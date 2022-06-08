package com.example.bookstore.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("User by username "+username+" doesn't exist");
    }
}
