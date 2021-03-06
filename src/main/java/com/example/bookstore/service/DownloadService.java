package com.example.bookstore.service;

import com.example.bookstore.entity.Image;
import com.example.bookstore.entity.Status;
import com.example.bookstore.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DownloadService {
    private final ImageService imageService;
    private final ImageRepository imageRepository;
    @Value("${images.location}")
    private String basePath;


//    @Scheduled(fixedRate = 3000)

    public void download() throws IOException {
        List<Image> images = imageRepository.findNotDownloaded("NOT_DOWNLOADED");
        System.out.println(">>>>>>>>>>>>> image count: " + images.size());
        for (Image image : images) {
            try (InputStream in = new URL(image.getFileURL()).openStream()) {
                imageService.updateImageStatus(image.getId(), Status.IN_PROGRESS);
                imageService.updateDownloadStart(image.getId(), new Timestamp(System.currentTimeMillis()));
                Files.copy(in, Paths.get(basePath + "/" + image.getId() + ".jpg"), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                image.setStatus(Status.FAILED);
                continue;
            }
            imageService.updateImageStatus(image.getId(), Status.DOWNLOADED);
            imageService.updateDownloadEnd(image.getId(), new Timestamp(System.currentTimeMillis()));
            System.out.println(">>>>>>>>>>>>>>>> image downloaded, image id = " + image.getId());
        }
    }
}
