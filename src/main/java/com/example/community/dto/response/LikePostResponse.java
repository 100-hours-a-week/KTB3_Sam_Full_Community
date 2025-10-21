package com.example.community.dto.response;

import com.example.community.entity.Like;

public record LikePostResponse(
        Long likeId,
        Long boardId,
        Long userId
) {
    public static LikePostResponse from(Like like) {
        return new LikePostResponse(like.getId(), like.getBoardId(), like.getUserId());
    }
}
