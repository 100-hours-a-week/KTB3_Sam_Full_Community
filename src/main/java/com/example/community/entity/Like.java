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

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    public Like(User author, Board post) {
        this.author = author;
        this.post = post;
    }

    @Override
    public Long getBoardId() {
        return this.post.getId();
    }

    @Override
    public Long getUserId() {
        return this.author.getId();
    }

    @Override
    public void setId(Long id) {this.id = id;}

    public void setAuthor(User author) {this.author = author;}

    public void setPost(Board post) {this.post = post;}
}
