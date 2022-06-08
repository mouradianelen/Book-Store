package com.example.bookstore.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "book", schema = "bookstore")
public class Book implements Serializable {
    @OneToMany(mappedBy = "book")
    Set<BookRating> ratings = new HashSet<>();
    @OneToMany(mappedBy = "book")
    Set<BookOrder> orders = new HashSet<>();
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "book_isbn")
    private String ISBN;
    @Column(name = "book_title")
    private String title;
    @Column(name = "image_url_s")
    private String imageURLS;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;
    @Column(name = "image_url_m")
    private String imageURLM;
    @Column(name = "page_count")
    private int pageCount;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Genre> genres = new HashSet<>();
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;
    @ManyToMany(mappedBy = "books")
    private List<Author> authors = new LinkedList<>();

    public Book() {

    }

    public void addAuthor(Author author) {
        this.authors.add(author);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id && ISBN.equals(book.ISBN) && title.equals(book.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ISBN, title);
    }
}
