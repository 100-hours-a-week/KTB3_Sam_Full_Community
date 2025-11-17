package com.example.community.dto.response;

import com.example.community.entity.Board;
import com.example.community.entity.Image;
import com.example.community.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record BoardDetailResponse(
        Long boardId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss")
        LocalDateTime updateAt,
        String title,
        Integer likes,
        Integer visitors,
        Integer commentsCount,
        List<Long> boardImageIds,
        String nickname,
        Long profileImageId
) {
    public static BoardDetailResponse of(Board board, Integer likes, Integer visitors, Integer commentsCount,List<Long> boardImageIds, User user, Image image) {
        return new BoardDetailResponse(board.getId(),board.getUpdatedAt(),  board.getTitle(), likes, visitors, commentsCount, boardImageIds, user.getNickname(), image.getId());
    }
}
