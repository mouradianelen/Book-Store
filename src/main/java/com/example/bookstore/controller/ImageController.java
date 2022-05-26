package com.example.bookstore.controller;

import com.example.bookstore.entity.Image;
import com.example.bookstore.service.ImageService;
import com.example.bookstore.service.DownloadService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;
    private final DownloadService downloadService;

    public ImageController(ImageService imageService, DownloadService downloadService) {
        this.imageService = imageService;
        this.downloadService = downloadService;
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Image> updateImage(@PathVariable Long id) {
//        return ResponseEntity.ok().body(imageService.updateImage(id));
//    }
//    @GetMapping()
//    public ResponseEntity<?> updateImage() throws IOException {
//        downloadService.download();
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }
    @GetMapping("/")
    public ResponseEntity<List<Image>> getPaginatedImages(@RequestParam(defaultValue = "0") Integer pageNo,
                                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                                          @RequestParam(defaultValue = "id") String sortBy
    ) {
        List<Image> list = imageService.getImagesPage(pageNo, pageSize,sortBy);
        return new ResponseEntity(list, new HttpHeaders(), HttpStatus.OK);


    }
    //pageable object


}
