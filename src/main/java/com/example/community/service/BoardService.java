package com.example.community.service;

import com.example.community.repository.BoardRepository;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }
}
