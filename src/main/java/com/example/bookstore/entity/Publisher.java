package com.example.bookstore.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "publisher", schema = "bookstore")
public class Publisher implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long publisherId;
    @Column(name = "publisher_name")
    private String publisherName;

    public Publisher(){

    }


    public long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(long publisherId) {
        this.publisherId = publisherId;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public Set<Book> getBook() {
        return book;
    }

    public void setBook(Set<Book> book) {
        this.book = book;
    }

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Book> book = new HashSet<>();
}
