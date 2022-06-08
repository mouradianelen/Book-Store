package com.example.bookstore.repository;

import com.example.bookstore.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    boolean existsByName(String name);
    Genre findByName(String name);
}
