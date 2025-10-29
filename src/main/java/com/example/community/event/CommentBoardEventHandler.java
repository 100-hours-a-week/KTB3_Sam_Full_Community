package com.example.community.event;

import com.example.community.service.CommentService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CommentBoardEventHandler {
    private final CommentService commentService;

    CommentBoardEventHandler(CommentService commentService) {
        this.commentService = commentService;
    }

    @EventListener
    public void handle(BoardDeletedEvent event) {
        commentService.deleteByBoardId(event.boardId());
    }
}
