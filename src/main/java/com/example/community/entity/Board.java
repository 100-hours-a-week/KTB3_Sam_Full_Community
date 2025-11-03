package com.example.community.entity;

import com.example.community.entity.interfaces.Identifiable;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Entity
public class Board extends BaseEntity implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "board_id")
    private Long id;
    private final AtomicInteger visitors = new AtomicInteger(0);
    private String title;
    private String content;
    private List<Long> boardImageIds = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();


    public Board(String title, String content, List<Long> boardImageIds, User user) {
        this.title = title;
        this.content = content;
        this.boardImageIds = boardImageIds;
        this.author = user;
    }

    public int recordVisit() {
        return this.visitors.incrementAndGet();
    }

    public int getVisitors() {
        return visitors.get();
    }

    public void updateBoard(String title, String content, List<Long> boardImageIds) {
        this.title = title;
        this.content = content;
        this.boardImageIds = boardImageIds;
    }

    @Override
    public void setId(Long id) {this.id = id;}

    public void setAuthor(User user) {
        this.author = user;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setPost(this);
    }

    public void addLike(Like like) {
        this.likes.add(like);
        like.setPost(this);
    }

    public void removeLike(Like like) {
        this.likes.remove(like);
        like.setPost(this);
    }
}
