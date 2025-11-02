package com.example.community.entity;

import com.example.community.entity.interfaces.BoardLinked;
import com.example.community.entity.interfaces.Identifiable;
import com.example.community.entity.interfaces.UserLinked;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Like extends BaseEntity implements BoardLinked, UserLinked, Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "like_id")
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

    @Override
    public void setId(Long id) {this.id = id;}
}
