package com.example.bookstore.service;

import com.example.bookstore.dto.UserDto;
import com.example.bookstore.dto.UserRatingDto;
import com.example.bookstore.entity.*;
import com.example.bookstore.repository.BookRatingRepository;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.UserRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BookRatingRepository bookRatingRepository;

    public UserService(UserRepository userRepository, BookRepository bookRepository, BookRatingRepository bookRatingRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.bookRatingRepository = bookRatingRepository;
    }

    public List<UserDto> createUsers(List<UserDto> userDtos){
        List<UserEntity> userList = userRepository.saveAll(UserDto.mapUserDtoToUserEntity(userDtos));
        return UserDto.mapUserEntityToUserDto(userList);
    }

    public List<UserDto> getUsersPage(int pageNo,int pageSize,String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<UserDto> pagedResult = userRepository.findUsersWithNoRatings(paging).map(UserDto::mapUserEntityToUserDto);
        if(pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<UserDto>();

    }
    public List<UserDto> uploadUserCSV(MultipartFile file) throws IOException {
        BufferedReader fileReader = new BufferedReader(new
                InputStreamReader(file.getInputStream(), "UTF-8"));

        List<UserDto> users = new CsvToBeanBuilder<UserDto>(fileReader)
                .withType(UserDto.class)
                .withSeparator(';')
                .withIgnoreEmptyLine(true)
                .withSkipLines(1)
                .build()
                .parse();
        return users;
    }
    @Async
    public List<UserDto> saveEntities(final MultipartFile file) throws IOException {
        List<UserDto> userDtos = uploadUserCSV(file);
        return createUsers(userDtos);
    }
    public List<UserRatingDto> uploadRatings(MultipartFile file) throws IOException{
        BufferedReader fileReader = new BufferedReader(new
                InputStreamReader(file.getInputStream(), "UTF-8"));

        List<UserRatingDto> ratings = new CsvToBeanBuilder<UserRatingDto>(fileReader)
                .withType(UserRatingDto.class)
                .withSeparator(';')
                .withIgnoreEmptyLine(true)
                .withSkipLines(1)
                .build()
                .parse();
        return ratings;
    }

    public void saveRatings(final MultipartFile file) throws IOException{
        List<UserRatingDto> userRatingDtos = uploadRatings(file);

        // Split the list into batches
        getBatches(userRatingDtos, 5000).forEach(this::processBatch);
//        Stack<BookRating> ratings = new Stack<>();
//        List<UserEntity> users = userRepository.findAll();
//        List<Book> books = bookRepository.findAll();
//        userRatingDtos.parallelStream().forEach(bookRating->{
//            UserEntity user = userRepository.findByUserId(bookRating.getUserId());
//            bookRepository.findByISBN(bookRating.getBookISBN()).ifPresent(book -> {
//                BookRating bookRating1 = new BookRating();
//                bookRating1.setRating(bookRating.getBookRating());
//                bookRating1.setUser(user);
//                bookRating1.setBook(book);
//                book.getRatings().add(bookRating1);
//                user.getRatings().add(bookRating1);
//                bookRatingRepository.save(bookRating1);
//            });
//
//        });
////        userRatingDtos.forEach(bookRating->{
//            UserEntity user = users.stream().filter(user1->bookRating.getUserId()==user1.getUserId()).findAny().orElse(null);
//            books.stream().filter(book1->bookRating.getBookISBN().equals(book1.getISBN())).findAny()
//                    .ifPresent( book -> {
//                        BookRating bookRating1 = new BookRating();
//                        bookRating1.setRating(bookRating.getBookRating());
//                        bookRating1.setUser(user);
//                        bookRating1.setBook(book);
//                        book.getRatings().add(bookRating1);
//                        user.getRatings().add(bookRating1);
//                        bookRatingRepository.save(bookRating1);
//                    });
//
//
//        });



    }
    private void processBatch(List<UserRatingDto> userRatingBatch) {

        // Retrieve all data required to process a batch
        Map<Long, UserEntity> users = userRepository
                .findAllByIdIn(userRatingBatch.stream()
                        .map(UserRatingDto::getUserId).collect(Collectors.toList()))
                .stream().filter(Optional :: isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(UserEntity::getId, user -> user));
        Map<String, Book> books = bookRepository
                .findAllByISBNIn(userRatingBatch.stream()
                        .map(UserRatingDto::getBookISBN).collect(Collectors.toList()))
                .stream().filter(Optional :: isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(Book::getISBN, book -> book));

        // Process each rating in memory
        List<BookRating> ratingsToSave = userRatingBatch.stream().map(bookRatingDto -> {
            Book book = books.get(bookRatingDto.getBookISBN());
            if (book == null) {
                return null;
            }
            UserEntity user = users.get(bookRatingDto.getUserId());
            BookRating bookRating = new BookRating();
            bookRating.setRating(bookRatingDto.getBookRating());
            bookRating.setUser(user);
            bookRating.setBook(book);
            book.getRatings().add(bookRating);
            user.getRatings().add(bookRating);
            return bookRating;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        // Save data in batches
        bookRatingRepository.saveAll(ratingsToSave);
        bookRepository.saveAll(books.values());
        userRepository.saveAll(users.values());

    }

    public <T> List<List<T>> getBatches(List<T> collection, int batchSize) {
        List<List<T>> batches = new ArrayList<>();
        for (int i = 0; i < collection.size(); i += batchSize) {
            batches.add(collection.subList(i, Math.min(i + batchSize, collection.size())));
        }
        return batches;
    }
}
