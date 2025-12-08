package com.example.community.dto.response;

import com.example.community.entity.Image;
import com.example.community.entity.User;

public record UserInfoResponse(
        Long userId,
        String email,
        String nickname,
        Long profileImageId
) {
    public static UserInfoResponse of(User user, Image image) {
        return new UserInfoResponse(user.getId(), user.getEmail(), user.getNickname(), image.getId());
    }
}
