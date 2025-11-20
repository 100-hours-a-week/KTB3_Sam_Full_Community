package com.example.community.event;

import com.example.community.service.CommentService;
import com.example.community.service.LikeService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BoardDeletedEventHandler {
    private final LikeService likeService;
    private final CommentService commentService;

    BoardDeletedEventHandler(LikeService likeService, CommentService commentService) {
        this.likeService = likeService;
        this.commentService = commentService;
    }

    @EventListener
    public void handle(BoardDeletedEvent event) {
        likeService.deleteByBoardId(event.boardId());
        commentService.deleteByBoardId(event.boardId());
    }
}
