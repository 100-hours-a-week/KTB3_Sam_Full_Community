package com.example.community.event;

import com.example.community.facade.BoardCommandFacade;
import com.example.community.service.BoardService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class UserBoardEventHandler {
    private final BoardService boardService;

    UserBoardEventHandler(BoardService boardService) {
        this.boardService = boardService;
    }

    @TransactionalEventListener
    public void handle(UserDeletedEvent event) {
        boardService.deleteByUserId(event.userId());
    }
}
