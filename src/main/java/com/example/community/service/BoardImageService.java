package com.example.community.service;

import com.example.community.entity.Board;
import com.example.community.entity.BoardImage;
import com.example.community.entity.Image;
import com.example.community.entity.User;
import com.example.community.repository.BoardImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardImageService {
    private final BoardImageRepository boardImageRepository;

    BoardImageService(BoardImageRepository boardImageRepository) {
        this.boardImageRepository = boardImageRepository;
    }

    public BoardImage save(Board board, Image image) {
        Optional<BoardImage> existingBoardImage = boardImageRepository.findByBoardIdAndImageId(board.getId(), image.getId());

        return existingBoardImage.orElseGet(() -> boardImageRepository.save(new BoardImage(board, image)));
    }
}
