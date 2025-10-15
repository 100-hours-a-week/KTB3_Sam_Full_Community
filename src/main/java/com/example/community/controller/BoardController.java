package com.example.community.controller;

import com.example.community.service.BoardService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoardController {
    private final BoardService boardService;

    BoardController(BoardService boardService) {
        this.boardService = boardService;
    }
}
