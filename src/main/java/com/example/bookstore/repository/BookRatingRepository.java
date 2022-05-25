package com.example.bookstore.repository;

import com.example.bookstore.entity.BookRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRatingRepository extends JpaRepository<BookRating,Long> {
}
