package com.example.bookstore.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "book", schema = "bookstore")
public class Book implements Serializable {
    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    Set<BookRating> ratings = new HashSet<>();
    @OneToMany(mappedBy = "book")
    Set<BookOrder> orders = new HashSet<>();
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long bookId;
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
    private Set<Author> authors = new HashSet<>();

    public Book() {

    }

    public Set<BookOrder> getOrders() {
        return orders;
    }

    public void setOrders(Set<BookOrder> orders) {
        this.orders = orders;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public Set<BookRating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<BookRating> ratings) {
        this.ratings = ratings;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public void addAuthor(Author author) {
        this.authors.add(author);
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long book_id) {
        this.bookId = book_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageURLS() {
        return imageURLS;
    }

    public void setImageURLS(String imageURLS) {
        this.imageURLS = imageURLS;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getImageURLM() {
        return imageURLM;
    }

    public void setImageURLM(String imageURLM) {
        this.imageURLM = imageURLM;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return bookId == book.bookId && ISBN.equals(book.ISBN) && title.equals(book.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, ISBN, title);
    }
}
