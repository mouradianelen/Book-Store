package com.example.bookstore.repository;


import com.example.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book,String> {
    @Query(
            value = "SELECT * FROM bookstore.book b  WHERE b.book_isbn = :isbn",
            nativeQuery = true)
    Optional<Book> findByISBN(String isbn);
    List<Optional<Book>> findAllByISBNIn(Iterable<String> isbns);


}
