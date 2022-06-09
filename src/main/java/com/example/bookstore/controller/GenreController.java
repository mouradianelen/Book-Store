package com.example.bookstore.controller;

import com.example.bookstore.dto.AssignGenreDto;
import com.example.bookstore.dto.GenreDto;
import com.example.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/genres")
public class GenreController {
    private final BookService bookService;

    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<GenreDto> createGenre(@RequestBody GenreDto genreDto) {
        return ResponseEntity.ok().body(bookService.createGenre(genreDto));
    }

    @Secured("ROLE_EDITOR")
    @PostMapping("/add-book")
    public ResponseEntity<AssignGenreDto> addGenreToBook(@RequestBody AssignGenreDto assignGenreDto) {
        return ResponseEntity.ok().body(bookService.assignGenreToBook(assignGenreDto));
    }
}
