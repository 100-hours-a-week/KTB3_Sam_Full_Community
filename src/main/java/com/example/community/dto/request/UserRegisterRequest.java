package com.example.community.dto.request;

public record UserRegisterRequest(
        String email,
        String password,
        String nickname,
        int profileImageId
) {
}
