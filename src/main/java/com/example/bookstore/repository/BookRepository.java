package com.example.bookstore.repository;


import com.example.bookstore.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, String> {

    List<Optional<Book>> findAllByISBNIn(Iterable<String> isbns);

    @Query(value = "SELECT ba.author_id, b.book_title\n" +
            "FROM bookstore.book_author ba JOIN bookstore.book b ON ba.book_id = b.id" +
            "    JOIN bookstore.author a ON ba.author_id = a.id\n" +
            "WHERE b.id IN (SELECT c.book_id\n" +
            "                    FROM bookstore.book_author c\n" +
            "                    GROUP BY c.book_id\n" +
            "                    HAVING count(c.book_id) > 1)",
            nativeQuery = true)
    Page<Book> findBooksWithMoreThanOneAuthor(Pageable paging);

    @Query(value = "SELECT r.book_id, b.*, AVG(r.rating) as average_rating\n" +
            "FROM bookstore.book b\n" +
            "INNER JOIN bookstore.book_rating as r ON r.book_id = b.id\n" +
            "GROUP BY b.id,r.book_id \n" +
            "ORDER BY average_rating DESC\n" +
            "LIMIT 20", nativeQuery = true)
    List<Book> findMostPopular();

    @Query(value = "select * from bookstore.book b where b.book_title=:title" +
            " limit 1", nativeQuery = true)
    Book findByTitle(String title);

    @Query("select distinct b from Book b join b.genres genres join genres.books where genres.name=:genreName")
    Page<Book> findBooksByGenre(String genreName, Pageable paging);

    @Query("select distinct b from Book b join b.authors authors join authors.books where authors.name=:author")
    Page<Book> findBooksByAuthor(String author, Pageable paging);


}
