package com.example.bookstore.controller;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.entity.Book;
import com.example.bookstore.service.BookService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

        bookService.saveEntities(file);
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @GetMapping("/more-authors")
    public ResponseEntity<List<Book>> getPaginatedUsers(@RequestParam(defaultValue = "0") Integer pageNo,
                                                        @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        List<BookDto> list = bookService.getBooksWithMoreThanOneAuthor(pageNo, pageSize);
        return new ResponseEntity(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Book>> getMostPopular() {
        List<BookDto> list = bookService.getMostPopularBooks();
        return new ResponseEntity(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping(value = "/get-image-with-media-type",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImageByBookTitle(@RequestParam String title) throws IOException {

        ClassPathResource result = bookService.getImageByBookTitle(title);
        byte[] bytes = StreamUtils.copyToByteArray(result.getInputStream());
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(bytes);
    }
}
