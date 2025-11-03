package com.example.community.event;

import com.example.community.facade.LikeCommandFacade;
import com.example.community.service.LikeService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class LikeBoardEventHandler {
    private final LikeCommandFacade likeCommandFacade;

    LikeBoardEventHandler(LikeCommandFacade likeCommandFacade) {
        this.likeCommandFacade = likeCommandFacade;
    }

    @EventListener
    public void handle(BoardDeletedEvent event) {
        likeCommandFacade.deleteLikeByBoardId(event.boardId());
    }
}
