package com.example.community.dto.response;

import com.example.community.entity.Board;

public record BoardPostResponse(
        Long boardId
) {
    public static BoardPostResponse from(Board board) {
        return new BoardPostResponse(board.getId());
    }
}
