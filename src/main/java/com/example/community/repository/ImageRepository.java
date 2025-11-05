package com.example.community.repository;

import com.example.community.entity.Image;
import com.example.community.repository.interfaces.ImageCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long>, ImageCustomRepository {
}
