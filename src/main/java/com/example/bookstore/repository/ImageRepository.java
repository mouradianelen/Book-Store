package com.example.bookstore.repository;

import com.example.bookstore.entity.Image;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends PagingAndSortingRepository<Image, Long> {

    Image findById(long id);

    @Query(value = "select i from Image i where i.status =:status")
    List<Image> findNotDownloaded(@Param("status") String status);


}
