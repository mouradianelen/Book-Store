package com.example.bookstore.repository;

import com.example.bookstore.entity.Image;
import com.example.bookstore.entity.Status;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ImageRepository extends PagingAndSortingRepository<Image,Long> {

    Image findById(long id);
    @Query(value = "select * from bookstore.image i where i.status =:status", nativeQuery = true)
    List<Image> findNotDownloaded(@Param("status") String status);

    @Query(value = "select i.* from bookstore.image i left join bookstore.book b on b.image_id=i.id " +
            "where b.book_title =:bookTitle ", nativeQuery = true)
    Image findImageUrl(@Param("bookTitle") String bookTitle);
}
