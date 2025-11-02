package com.example.community.entity;

import com.example.community.entity.interfaces.BoardLinked;
import com.example.community.entity.interfaces.UserLinked;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class Like extends BaseEntity implements BoardLinked, UserLinked {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
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
