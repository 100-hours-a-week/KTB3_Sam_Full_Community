package com.example.community.repository;

import com.example.community.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {
    @Query("select u from UserImage u join fetch u.user join fetch u.image where u.user.id = :userId and u.image.id = :imageId")
    Optional<UserImage> findByUserIdAndImageId(@Param("userId") Long userId, @Param("imageId") Long imageId);

    @Query("select u from UserImage u join fetch u.user join fetch u.image where u.user.id = :userId")
    Optional<UserImage> findByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("delete from UserImage ui where ui.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
