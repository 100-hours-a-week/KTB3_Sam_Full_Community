package com.example.community.entity;

import com.example.community.entity.interfaces.BoardLinked;
import com.example.community.entity.interfaces.UserLinked;
import lombok.Getter;

@Getter
public class Like extends BaseEntity implements BoardLinked, UserLinked {
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

    @Override
    public Long getUserId() {
        return this.userId;
    }
}
