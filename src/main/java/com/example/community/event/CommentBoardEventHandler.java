package com.example.community.event;

import com.example.community.facade.CommentCommandFacade;
import com.example.community.service.CommentService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CommentBoardEventHandler {
    private final CommentCommandFacade commentCommandFacade;

    CommentBoardEventHandler(CommentCommandFacade commentCommandFacade) {
        this.commentCommandFacade = commentCommandFacade;
    }

    @EventListener
    public void handle(BoardDeletedEvent event) {
        commentCommandFacade.deleteByBoardId(event.boardId());
    }
}
