package com.example.community.dto.response;

import com.example.community.entity.Board;
import com.example.community.entity.Image;
import com.example.community.entity.User;
import com.example.community.entity.UserImage;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record BoardInfoResponse(
        Long boardId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss")
        LocalDateTime updateAt,
        String title,
        Integer likes,
        Integer visitors,
        Integer commentsCount,
        String nickname,
        Long profileImageId
) {
    public static BoardInfoResponse of(Board board, Integer likes,Integer visitors, Integer commentsCount, User user, Image image) {
        return new BoardInfoResponse(board.getId(),board.getUpdatedAt(),  board.getTitle(), likes, visitors, commentsCount,user.getNickname(), image.getId());
    }

}
