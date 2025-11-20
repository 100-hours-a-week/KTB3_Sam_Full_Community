package com.example.community.dto.response;

public record BoardLikedCheckResponse(
        Boolean isLiked
) {
    public static BoardLikedCheckResponse from(Boolean isLiked) {
        return new BoardLikedCheckResponse(isLiked);
    }
}
