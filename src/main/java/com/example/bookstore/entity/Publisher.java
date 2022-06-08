package com.example.bookstore.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "publisher", schema = "bookstore")
public class Publisher implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "publisher_name")
    private String publisherName;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Book> book = new HashSet<>();

    public Publisher() {

    }


    public Publisher(String name) {
        this.publisherName = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publisher publisher = (Publisher) o;
        return id == publisher.id && publisherName.equals(publisher.publisherName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, publisherName);
    }
}
