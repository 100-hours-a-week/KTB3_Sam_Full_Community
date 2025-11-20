package com.example.community.dto.response;

import com.example.community.entity.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record CommentInfoResponse(
        Long commentId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss")
        LocalDateTime updateAt,
        String content,
        Long boardId,
        String nickname,
        Long profileImageId
) {
    public static CommentInfoResponse from(Comment comment, String nickname, Long profileImageId) {
        return new CommentInfoResponse(comment.getId(), comment.getUpdatedAt(), comment.getContent(), comment.getBoardId(), nickname, profileImageId);
    }
}
