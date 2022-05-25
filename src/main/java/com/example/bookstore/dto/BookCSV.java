package com.example.bookstore.dto;

import com.opencsv.bean.CsvBindByPosition;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "book", schema = "school")
public class BookCSV {

    @Id
    @CsvBindByPosition(position = 0)
    private String bookId;
    @CsvBindByPosition(position = 1)
    @Column(name = "title")
    private String title;
    @CsvBindByPosition(position = 2)
    @Column(name = "author")
    private String author;
    @CsvBindByPosition(position = 3)
    @Column(name = "publication_date")
    private String date;
    @CsvBindByPosition(position = 4)
    @Column(name = "publisher")
    private String publisher;
    @CsvBindByPosition(position = 5)
    @Column(name = "image_url_s")
    private String imageURLS;
    @CsvBindByPosition(position = 6)
    @Column(name = "image_url_m")
    private String imageURLM;
    @CsvBindByPosition(position = 7)
    @Column(name = "image_url_l")
    private String imageURLL;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getImageURLS() {
        return imageURLS;
    }

    public void setImageURLS(String imageURLS) {
        this.imageURLS = imageURLS;
    }

    public String getImageURLM() {
        return imageURLM;
    }

    public void setImageURLM(String imageURLM) {
        this.imageURLM = imageURLM;
    }

    public String getImageURLL() {
        return imageURLL;
    }

    public void setImageURLL(String imageURLL) {
        this.imageURLL = imageURLL;
    }

    public BookCSV() {

    }
}
