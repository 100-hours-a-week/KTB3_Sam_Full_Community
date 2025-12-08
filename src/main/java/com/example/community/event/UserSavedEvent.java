package com.example.community.event;

public record UserSavedEvent(
        Long userId,
        Long profileImageId
) {
}
