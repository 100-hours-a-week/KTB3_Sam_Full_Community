package com.example.community.facade;

import com.example.community.service.BoardService;
import com.example.community.service.CommentService;
import com.example.community.service.LikeService;
import org.springframework.stereotype.Service;

@Service
public class BoardCommandFacade {
    private final BoardService boardService;
    private final CommentService commentService;
    private final LikeService likeService;

    BoardCommandFacade(BoardService boardService, CommentService commentService, LikeService likeService) {
        this.boardService = boardService;
        this.commentService = commentService;
        this.likeService = likeService;
    }


}
