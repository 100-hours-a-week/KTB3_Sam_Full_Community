package com.example.community.dto;

public record AuthToken(
        String accessToken,
        String refreshToken
) {
}
