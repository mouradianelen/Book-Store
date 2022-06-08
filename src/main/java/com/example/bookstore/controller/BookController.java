package com.example.bookstore.controller;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.dto.BookRatingDto;
import com.example.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;


    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

        bookService.saveEntities(file);
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @PostMapping
    public ResponseEntity<BookDto> addBook(@RequestBody BookDto bookDto) {
        return ResponseEntity.ok().body(bookService.addBook(bookDto));
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> getBooks(@RequestParam(defaultValue = "0") Integer pageNo,
                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                  @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok().body(bookService.getBooks(pageNo, pageSize, sortBy));
    }

    @GetMapping("genre/{genre}")
    public ResponseEntity<List<BookDto>> getBooksByGenre(@RequestParam(defaultValue = "0") Integer pageNo,
                                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                                         @RequestParam(defaultValue = "id") String sortBy,
                                                         @PathVariable("genre") String genre) {
        return ResponseEntity.ok().body(bookService.getBooksByGenre(pageNo, pageSize, sortBy, genre));
    }

    @GetMapping("/author")
    public ResponseEntity<List<BookDto>> getBooksByAuthor(@RequestParam(defaultValue = "0") Integer pageNo,
                                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                                          @RequestParam(defaultValue = "id") String sortBy,
                                                          @RequestParam String author) {
        return ResponseEntity.ok().body(bookService.getBooksByAuthor(pageNo, pageSize, sortBy, author));
    }
    @GetMapping("/search")
    public ResponseEntity<BookDto> getBookByTitle(@RequestParam String title){
        return ResponseEntity.ok().body(bookService.getBookByTitle(title));
    }

    @PostMapping("/rate")
    public ResponseEntity<BookRatingDto> rateBook(@RequestBody BookRatingDto bookRatingDto) {
        return ResponseEntity.ok().body(bookService.rateBook(bookRatingDto));
    }


    @GetMapping("/more-authors")
    public ResponseEntity<List<BookDto>> getPaginatedUsers(@RequestParam(defaultValue = "0") Integer pageNo,
                                                           @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        List<BookDto> list = bookService.getBooksWithMoreThanOneAuthor(pageNo, pageSize);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/popular")
    @Secured("ROLE_USER")
    public ResponseEntity<List<BookDto>> getMostPopular() {
        List<BookDto> list = bookService.getMostPopularBooks();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/image",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getImageWithMediaType(@RequestParam String title) throws IOException {
        return bookService.getImageByBookTitle(title);
    }
}
