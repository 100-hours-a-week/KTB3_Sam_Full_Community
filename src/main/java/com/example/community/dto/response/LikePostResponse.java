package com.example.community.dto.response;

import com.example.community.entity.Like;

public record LikePostResponse(
        Long likeId,
        Long boardId,
        Long userId
) {
    public static LikePostResponse of(Like like, Long boardId, Long userId) {
        return new LikePostResponse(like.getId(), boardId,userId);
    }
}
