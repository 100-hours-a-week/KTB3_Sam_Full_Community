package com.example.community.repository;

import com.example.community.entity.Image;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;

@Repository
public class ImageRepository {
    private Map<Long, Image> imageDB;
    private long sequence = 0L;

    ImageRepository() {
        this.imageDB = new LinkedHashMap<>();
    }
}
