package com.example.community.facade;

import com.example.community.entity.Board;
import com.example.community.entity.Image;
import com.example.community.service.BoardImageService;
import com.example.community.service.BoardService;
import com.example.community.service.ImageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardImageCommandFacade {
    private final BoardImageService boardImageService;
    private final ImageService imageService;
    private final BoardService boardService;

    BoardImageCommandFacade(BoardImageService boardImageService, ImageService imageService, BoardService boardService) {
        this.boardImageService = boardImageService;
        this.imageService = imageService;
        this.boardService = boardService;
    }

    public void mapsImagesToBoard(Long boardId, List<Long> imageIds) {
        Board board = boardService.findById(boardId);
        List<Image> images = imageService.findByIds(imageIds);

        images.forEach(image -> boardImageService.save(board, image));
    }
}
