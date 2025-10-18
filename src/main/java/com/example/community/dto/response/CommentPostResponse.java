package com.example.community.dto.response;

import com.example.community.entity.Comment;

public record CommentPostResponse(
        Long commentId
) {
    public static CommentPostResponse from(Comment comment) {
        return new CommentPostResponse(comment.getId());
    }
}
