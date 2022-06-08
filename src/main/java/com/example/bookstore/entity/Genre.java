package com.example.bookstore.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "genre", schema = "bookstore")
public class Genre implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "genre_description")
    private String genreDescription;
    @Column(name = "genre_name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    public Genre() {

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return id == genre.id && genreDescription.equals(genre.genreDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, genreDescription);
    }
}
