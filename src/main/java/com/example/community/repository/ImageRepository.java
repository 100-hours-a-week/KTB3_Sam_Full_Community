package com.example.community.repository;

import com.example.community.entity.Image;
import com.example.community.entity.User;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class ImageRepository {
    private Map<Long, Image> imageDB;
    private long sequence = 0L;

    ImageRepository() {
        this.imageDB = new LinkedHashMap<>();
    }

    public Image save(Image image) {
        if(image.getId() == null) {
            image.setId(++sequence);
        }
        imageDB.put(image.getId(), image);
        return image;
    }

    public Optional<Image> findById(Long id) {
        return Optional.ofNullable(imageDB.get(id));
    }
}
