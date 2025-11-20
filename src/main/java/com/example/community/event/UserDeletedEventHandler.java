package com.example.community.event;

import com.example.community.service.BoardService;
import com.example.community.service.UserImageService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserDeletedEventHandler {
    private final BoardService boardService;
    private final UserImageService userImageService;

    UserDeletedEventHandler(BoardService boardService, UserImageService userImageService) {
        this.boardService = boardService;
        this.userImageService = userImageService;
    }

    @EventListener
    public void handle(UserDeletedEvent event) {
        boardService.deleteByUserId(event.userId());
        userImageService.deleteByUserId(event.userId());
    }
}
