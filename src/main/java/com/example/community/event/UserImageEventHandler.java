package com.example.community.event;

import com.example.community.facade.UserImageCommandFacade;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserImageEventHandler {
    private final UserImageCommandFacade userImageCommandFacade;

    UserImageEventHandler(UserImageCommandFacade userImageCommandFacade) {
        this.userImageCommandFacade = userImageCommandFacade;
    }


    @EventListener
    public void handle(UserSavedEvent event) {
        userImageCommandFacade.mapsImagesToUser(event.userId(), event.profileImageId());
    }
}
