package com.example.community.entity;

import com.example.community.entity.interfaces.BoardLinked;
import com.example.community.entity.interfaces.Identifiable;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Comment extends BaseEntity implements BoardLinked, Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "comment_id")
    private Long id;
    private String content;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Comment(User user, Board board, String content) {
        this.user = user;
        this.board = board;
        this.content = content;
    }

    public void updateComment(String content) {
        this.content = content;
    }

    @Override
    public Long getBoardId() {
        return this.board.getId();
    }

    @Override
    public void setId(Long id) {this.id =id;}

    public void setUser(User user) {this.user = user;}

    public void setBoard(Board board) {this.board = board;}
}
