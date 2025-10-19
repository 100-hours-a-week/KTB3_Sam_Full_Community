package com.example.community.service;

import com.example.community.entity.Image;
import com.example.community.repository.ImageRepository;
import org.springframework.stereotype.Service;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Long makeImage() {
        Image image = imageRepository.save(new Image());
        return image.getId();
    }
}
