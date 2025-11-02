package com.example.community.service;

import com.example.community.entity.Image;
import com.example.community.repository.inmemory.InMemoryImageRepository;
import org.springframework.stereotype.Service;

@Service
public class ImageService {
    private final InMemoryImageRepository inMemoryImageRepository;

    ImageService(InMemoryImageRepository inMemoryImageRepository) {
        this.inMemoryImageRepository = inMemoryImageRepository;
    }

    public Long makeImage() {
        Image image = inMemoryImageRepository.save(new Image());
        return image.getId();
    }
}
