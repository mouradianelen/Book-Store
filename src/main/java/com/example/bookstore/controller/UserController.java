package com.example.bookstore.controller;

import com.example.bookstore.dto.UserCSVDto;
import com.example.bookstore.dto.UserDto;
import com.example.bookstore.service.AuthenticationService;
import com.example.bookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final AuthenticationService authenticationService;


    @PostMapping
    public ResponseEntity<List<UserCSVDto>> createUsers(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok().body(userService.saveEntities(file));
    }

    @PostMapping("/add-ratings")
    public ResponseEntity<?> addRatings(@RequestParam("file") MultipartFile file) throws IOException {
        userService.saveRatings(file);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/no-ratings")
    public ResponseEntity<List<UserCSVDto>> getPaginatedUsers(@RequestParam(defaultValue = "0") Integer pageNo,
                                                              @RequestParam(defaultValue = "10") Integer pageSize,
                                                              @RequestParam(defaultValue = "id") String sortBy
    ) {
        List<UserCSVDto> list = userService.getUsersPage(pageNo, pageSize, sortBy);
        return ResponseEntity.ok().body(list);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUserAccount(
            @Valid @RequestBody UserDto userDto) {

        UserDto registered = authenticationService.registerNewUserAccount(userDto);
        return ResponseEntity.ok().body(registered);
    }

    @PostMapping("/admin/{username}")
    public ResponseEntity<UserDto> makeAdmin(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(authenticationService.addNewAdmin(username));
    }
}

