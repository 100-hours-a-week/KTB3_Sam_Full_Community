package com.example.community.service;

import com.example.community.entity.Image;
import com.example.community.repository.ImageRepository;
import com.example.community.repository.inmemory.InMemoryImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Image> findByIds(List<Long> imageIds) {
        return imageRepository.findByIds(imageIds);
    }
}
