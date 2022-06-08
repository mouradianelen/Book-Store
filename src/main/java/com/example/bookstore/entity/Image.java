package com.example.bookstore.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "image", schema = "bookstore")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private String fileURL;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column
    private Timestamp downloadStart;
    @Column
    private Timestamp downloadEnd;


    public Image() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return id == image.id && Objects.equals(fileURL, image.fileURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileURL);
    }
}
