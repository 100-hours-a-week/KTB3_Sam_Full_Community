package com.example.community.event;

import com.example.community.service.LikeService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class LikeBoardEventHandler {
    private final LikeService likeService;

    LikeBoardEventHandler(LikeService likeService) {
        this.likeService = likeService;
    }

    @EventListener
    public void handle(BoardDeletedEvent event) {
        likeService.deleteByBoardId(event.boardId());
    }
}
