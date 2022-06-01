package com.example.bookstore.repository;

import com.example.bookstore.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {

    UserEntity findByUsername(String username);
    UserEntity findByEmail(String email);
    @Query(
            value = "SELECT * FROM bookstore.store_user u WHERE u.user_id = :id",
            nativeQuery = true)
    UserEntity findByUserId(long id);

    @Query("SELECT u FROM UserEntity u WHERE u.userId IN(:ids)")
    List<UserEntity> findAllByIdIn(@Param("ids") List<Long> ids);

//    @Query(value = "select u.* from bookstore.store_user u " +
//            "left join bookstore.book_rating b on u.id = b.user_id " +
//            "where b.user_id is null ", nativeQuery = true)
    @Query("select u from UserEntity u left join BookRating b on u.id=b.user.id " +
            "where b.user is null")
    Page<UserEntity> findUsersWithNoRatings(Pageable paging);

}
