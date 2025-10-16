package com.example.community.service;

import com.example.community.repository.LikeRepository;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    private final LikeRepository likeRepository;

    LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }
}
