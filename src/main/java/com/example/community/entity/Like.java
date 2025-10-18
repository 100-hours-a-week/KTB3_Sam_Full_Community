package com.example.community.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Like extends BaseEntity{
    private Long id;
    private Long boardId;
    private Long userId;

    public Like(Long userId, Long boardId) {
        this.userId = userId;
        this.boardId = boardId;
    }
}
