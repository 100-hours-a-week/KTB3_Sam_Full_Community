package com.example.community.event;

import com.example.community.facade.BoardImageCommandFacade;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BoardImageEventHandler {
    private final BoardImageCommandFacade boardImageCommandFacade;

    BoardImageEventHandler(BoardImageCommandFacade boardImageCommandFacade) {
        this.boardImageCommandFacade = boardImageCommandFacade;
    }

    @EventListener
    public void handle(BoardSavedEvent event) {
        boardImageCommandFacade.mapsImagesToBoard(event.boardId(), event.boardImageIds());
    }
}
