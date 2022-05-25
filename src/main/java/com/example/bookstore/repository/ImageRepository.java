package com.example.bookstore.repository;

import com.example.bookstore.entity.Image;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ImageRepository extends PagingAndSortingRepository<Image,Long> {
//    @Modifying
//    @Query("update Image i set i.inProgress = true where i.id = :id")
//    void updateImage(@Param(value = "id") long id);
    Image findById(long id);
    @Query( "SELECT i FROM Image i WHERE i.inProgress = false")
    List<Image> findProcessed();
}
