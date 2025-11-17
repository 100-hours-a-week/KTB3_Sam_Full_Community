package com.example.community.repository;

import com.example.community.entity.Image;
import com.example.community.repository.interfaces.ImageCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image,Long>, ImageCustomRepository {
    @Query("select i from Image i where i.id in :imageIds")
    List<Image> findByIds(@Param("imageIds") List<Long> imageIds);
}
