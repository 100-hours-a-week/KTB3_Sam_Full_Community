package com.example.community.event.handler;

import com.example.community.event.BoardSavedEvent;
import com.example.community.facade.BoardImageCommandFacade;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BoardSavedEventHandler {
    private final BoardImageCommandFacade boardImageCommandFacade;

    BoardSavedEventHandler(BoardImageCommandFacade boardImageCommandFacade) {
        this.boardImageCommandFacade = boardImageCommandFacade;
    }

    @EventListener
    public void handle(BoardSavedEvent event) {
        boardImageCommandFacade.mapsImagesToBoard(event.boardId(), event.boardImageIds());
    }
}
