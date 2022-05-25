package com.example.bookstore.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "genre", schema = "bookstore")
public class Genre implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long genreId;
    @Column(name = "genre_description")
    private String genreDescription;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    public Genre(){

    }
    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public long getGenreId() {
        return genreId;
    }

    public void setGenreId(long genreId) {
        this.genreId = genreId;
    }

    public String getGenreDescription() {
        return genreDescription;
    }

    public void setGenreDescription(String genreDescription) {
        this.genreDescription = genreDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return genreId == genre.genreId && genreDescription.equals(genre.genreDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genreId, genreDescription);
    }
}
