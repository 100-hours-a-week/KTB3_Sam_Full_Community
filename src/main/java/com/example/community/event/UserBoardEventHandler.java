package com.example.community.event;

import com.example.community.facade.BoardCommandFacade;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class UserBoardEventHandler {
    private final BoardCommandFacade boardCommandFacade;

    UserBoardEventHandler(BoardCommandFacade boardCommandFacade) {
        this.boardCommandFacade = boardCommandFacade;
    }

    @TransactionalEventListener
    public void handle(UserDeletedEvent event) {
        boardCommandFacade.deleteByUserId(event.userId());
    }
}
