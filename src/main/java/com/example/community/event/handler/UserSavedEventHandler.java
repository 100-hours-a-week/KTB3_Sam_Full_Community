package com.example.community.event.handler;

import com.example.community.event.UserSavedEvent;
import com.example.community.facade.UserImageCommandFacade;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserSavedEventHandler {
    private final UserImageCommandFacade userImageCommandFacade;

    UserSavedEventHandler(UserImageCommandFacade userImageCommandFacade) {
        this.userImageCommandFacade = userImageCommandFacade;
    }


    @EventListener
    public void handle(UserSavedEvent event) {
        userImageCommandFacade.mapsImagesToUser(event.userId(), event.profileImageId());
    }
}
