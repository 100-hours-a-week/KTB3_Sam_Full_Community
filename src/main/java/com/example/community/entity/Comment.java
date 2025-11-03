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
    private Board post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    public Comment(User author, Board post, String content) {
        this.author = author;
        this.post = post;
        this.content = content;
    }

    public void updateComment(String content) {
        this.content = content;
    }

    @Override
    public Long getBoardId() {
        return this.post.getId();
    }

    @Override
    public void setId(Long id) {this.id =id;}

    public void setAuthor(User author) {this.author = author;}

    public void setPost(Board post) {this.post = post;}
}
