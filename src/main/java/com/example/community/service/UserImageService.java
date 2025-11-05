package com.example.community.service;

import com.example.community.repository.UserImageRepository;
import org.springframework.stereotype.Service;

@Service
public class UserImageService {
    private final UserImageRepository userImageRepository;

    UserImageService(UserImageRepository userImageRepository) {
        this.userImageRepository = userImageRepository;
    }
}
