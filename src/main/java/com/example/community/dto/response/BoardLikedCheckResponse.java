package com.example.community.dto.response;

public record BoardLikedCheckResponse(
        boolean isLiked
) {
    public static BoardLikedCheckResponse from(Boolean isLiked) {
        return new BoardLikedCheckResponse(isLiked);
    }
}
