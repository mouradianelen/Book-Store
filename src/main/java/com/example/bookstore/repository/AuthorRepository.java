package com.example.bookstore.repository;

import com.example.bookstore.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuthorRepository extends JpaRepository<Author,Long> {
    @Query(value = "select * from bookstore.author where" +
            " author.name=:name limit 1", nativeQuery = true)
    Author findByName(String name);
}
