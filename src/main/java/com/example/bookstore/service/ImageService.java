package com.example.bookstore.service;

import com.example.bookstore.entity.Image;
import com.example.bookstore.entity.Status;
import com.example.bookstore.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;


    @Transactional
    public void updateImageStatus(long id, Status status) {
        Image image = imageRepository.findById(id);
        image.setStatus(status);
        imageRepository.save(image);
    }

    @Transactional
    public void updateDownloadStart(long id, Timestamp timestamp) {
        Image image = imageRepository.findById(id);
        image.setDownloadStart(timestamp);
        imageRepository.save(image);
    }

    @Transactional
    public void updateDownloadEnd(long id, Timestamp timestamp) {
        Image image = imageRepository.findById(id);
        image.setDownloadEnd(timestamp);
        imageRepository.save(image);
    }

    public List<Image> getImagesPage(int pageNo, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Image> pagedResult = imageRepository.findAll(paging);
        if (pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();

    }

}




