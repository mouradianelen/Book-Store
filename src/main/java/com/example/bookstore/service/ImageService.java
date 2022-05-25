package com.example.bookstore.service;

import com.example.bookstore.entity.Image;
import com.example.bookstore.repository.ImageRepository;
import org.springframework.core.env.Environment;
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
public class ImageService {


    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository, Environment environment) {
        this.imageRepository = imageRepository;
    }

    @Transactional
    public Image updateImage(long id) {
        Image image = imageRepository.findById(id);
        image.setInProgress(true);
        return imageRepository.save(image);
    }

    @Transactional
    public Image updateDownloadStart(long id, Timestamp timestamp) {
        Image image = imageRepository.findById(id);
        image.setDownloadStart(timestamp);
        return imageRepository.save(image);
    }

    @Transactional
    public Image updateDownloadEnd(long id, Timestamp timestamp) {
        Image image = imageRepository.findById(id);
        image.setDownloadEnd(timestamp);
        return imageRepository.save(image);
    }
    public List<Image> getImagesPage(int pageNo,int pageSize,String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Image> pagedResult = imageRepository.findAll(paging);
        if(pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<Image>();

    }

}




