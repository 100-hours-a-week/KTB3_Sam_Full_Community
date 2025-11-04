package com.example.community.service;

import com.example.community.entity.BoardImage;
import com.example.community.repository.BoardImageRepository;
import org.springframework.stereotype.Service;

@Service
public class BoardImageService {
    private final BoardImageRepository boardImageRepository;

    BoardImageService(BoardImageRepository boardImageRepository) {
        this.boardImageRepository = boardImageRepository;
    }
}
