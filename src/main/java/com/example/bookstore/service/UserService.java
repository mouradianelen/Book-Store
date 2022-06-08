package com.example.bookstore.service;

import com.example.bookstore.dto.UserCSVDto;
import com.example.bookstore.dto.UserRatingDto;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.BookRating;
import com.example.bookstore.entity.UserEntity;
import com.example.bookstore.repository.BookRatingRepository;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.UserRepository;
import com.example.bookstore.service.util.ServiceUtil;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BookRatingRepository bookRatingRepository;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);


    public List<UserCSVDto> createUsers(List<UserCSVDto> userCSVDtos) {
        List<UserEntity> userList = userRepository.saveAll(UserCSVDto.mapUserDtoToUserEntity(userCSVDtos));
        return UserCSVDto.mapUserEntityToUserDto(userList).subList(0, 20);
    }

    public List<UserCSVDto> getUsersPage(int pageNo, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<UserCSVDto> pagedResult = userRepository.findUsersWithNoRatings(paging).map(UserCSVDto::mapUserEntityToUserDto);
        if (pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();

    }

    public List<UserCSVDto> uploadUserCSV(MultipartFile file) throws IOException {
        BufferedReader fileReader = new BufferedReader(new
                InputStreamReader(file.getInputStream(), "UTF-8"));

        return new CsvToBeanBuilder<UserCSVDto>(fileReader)
                .withType(UserCSVDto.class)
                .withSeparator(';')
                .withIgnoreEmptyLine(true)
                .withSkipLines(1)
                .build()
                .parse();

    }

    public List<UserCSVDto> saveEntities(final MultipartFile file) throws IOException {
        List<UserCSVDto> userCSVDtos = uploadUserCSV(file);
        return createUsers(userCSVDtos);
    }

    public List<UserRatingDto> uploadRatings(MultipartFile file) throws IOException {
        BufferedReader fileReader = new BufferedReader(new
                InputStreamReader(file.getInputStream(), "UTF-8"));

        return new CsvToBeanBuilder<UserRatingDto>(fileReader)
                .withType(UserRatingDto.class)
                .withSeparator(';')
                .withIgnoreEmptyLine(true)
                .withSkipLines(1)
                .build()
                .parse();
    }

    public void saveRatings(final MultipartFile file) throws IOException {
        List<UserRatingDto> userRatingDtos = uploadRatings(file);
        List<CompletableFuture<Void>> callables = new ArrayList<>();
        ServiceUtil.getBatches(userRatingDtos, 1000).forEach(userRatings -> {
            callables.add(createTask(userRatings));
        });

        for (CompletableFuture<Void> callable : callables) {
        }
    }

    private CompletableFuture<Void> createTask(List<UserRatingDto> userRatingDtos) {
        return CompletableFuture.supplyAsync(() -> {
            String uuid = UUID.randomUUID().toString();
            System.out.println(">>>>>>>>>>>>>>>>>>>> execution started, id: " + uuid);
            processBatch(userRatingDtos);
            System.out.println(">>>>>>>>>>>>>>>>>>>> execution ended, id: " + uuid);
            return null;
        }, executor);
    }

    private void processBatch(List<UserRatingDto> userRatingBatch) {
        List<Long> userIds = userRatingBatch.stream()
                .map(UserRatingDto::getUserId).collect(Collectors.toList());
        List<UserEntity> users = userRepository.findAllByIdIn(userIds);
        Map<String, Book> books = bookRepository
                .findAllByISBNIn(userRatingBatch.stream()
                        .map(UserRatingDto::getBookISBN).collect(Collectors.toList()))
                .stream().filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(Book::getISBN, book -> book));
        List<BookRating> ratingsToSave = userRatingBatch.stream().map(bookRatingDto -> {
            Book book = books.get(bookRatingDto.getBookISBN());
            if (book == null) {
                return null;
            }
            UserEntity user = users.stream().filter(userEntity -> userEntity.getUserId() == bookRatingDto.getUserId()).findFirst().orElse(null);
            BookRating bookRating = new BookRating();
            bookRating.setRating(bookRatingDto.getBookRating());
            bookRating.setUser(user);
            bookRating.setBook(book);
            return bookRating;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        bookRatingRepository.saveAll(ratingsToSave);
    }


}
