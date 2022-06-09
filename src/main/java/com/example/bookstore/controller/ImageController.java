package com.example.bookstore.controller;

import com.example.bookstore.entity.Image;
import com.example.bookstore.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;

    @Secured({"ROLE_EDITOR", "ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/")
    public ResponseEntity<List<Image>> getPaginatedImages(@RequestParam(defaultValue = "0") Integer pageNo,
                                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                                          @RequestParam(defaultValue = "id") String sortBy
    ) {
        List<Image> list = imageService.getImagesPage(pageNo, pageSize, sortBy);
        return new ResponseEntity(list, new HttpHeaders(), HttpStatus.OK);


    }

}
