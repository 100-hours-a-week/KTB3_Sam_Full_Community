package com.example.community.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Like extends BaseEntity implements BoardLinked{
    private Long boardId;
    private Long userId;

    public Like(Long userId, Long boardId) {
        this.userId = userId;
        this.boardId = boardId;
    }

    @Override
    public Long getBoardId() {
        return this.boardId;
    }
}
