package com.example.community.entity;

import com.example.community.entity.interfaces.BoardLinked;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class Comment extends BaseEntity implements BoardLinked {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String content;
    private Long boardId;
    private Long userId;

    public Comment(Long userId, Long boardId, String content) {
        this.userId = userId;
        this.boardId = boardId;
        this.content = content;
    }

    public void updateComment(String content) {
        this.content = content;
    }

    @Override
    public Long getBoardId() {
        return this.boardId;
    }
}
