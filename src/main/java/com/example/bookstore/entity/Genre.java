package com.example.bookstore.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
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
    @ManyToMany
    @JoinTable(name = "book_genre", schema = "bookstore",
            joinColumns = @JoinColumn(name = "genre_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> books = new LinkedList<>();

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
