package com.example.community.dto.response;

import com.example.community.entity.User;

public record UserInfoResponse(
        Long userId,
        String email,
        String nickname,
        Long profileImageId
) {
    public static UserInfoResponse from(User user) {
        return new UserInfoResponse(user.getId(), user.getEmail(), user.getNickname(), user.getProfileImageId());
    }
}
