package com.example.community.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Comment extends BaseEntity{
    private Long id;
    private String content;
    private Long boardId;
    private Long userId;

    public Comment(Long userId, Long boardId, String content) {
        this.userId = userId;
        this.boardId = boardId;
        this.content = content;
    }
}
