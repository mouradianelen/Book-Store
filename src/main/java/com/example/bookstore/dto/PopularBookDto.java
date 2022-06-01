package com.example.bookstore.dto;

import com.example.bookstore.entity.Author;

import java.util.HashSet;

public class PopularBookDto {
    private String ISBN;
    private String title;
    private HashSet<Author> authors;
    private double averageRating;

    public PopularBookDto(String ISBN, String title, HashSet<Author> authors, double averageRating) {
        this.ISBN = ISBN;
        this.title = title;
        this.authors = authors;
        this.averageRating = averageRating;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
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

    public HashSet<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(HashSet<Author> authors) {
        this.authors = authors;
    }
}


