package com.example.community.dto.response;

import com.example.community.dto.request.UserRegisterRequest;
import com.example.community.entity.User;

public record UserRegisterResponse(
        Long userId
) {
    public static UserRegisterResponse from(User user) {
        return new UserRegisterResponse(user.getId());
    }
}
