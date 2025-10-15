package com.example.community.repository;

import com.example.community.entity.Image;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;

@Repository
public class ImageRepository {
    private final Map<Long, Image> ImageDB;

    ImageRepository() {
        this.ImageDB = new LinkedHashMap<>();
    }
}
