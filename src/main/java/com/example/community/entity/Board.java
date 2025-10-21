package com.example.community.entity;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class Board extends BaseEntity{
    private final AtomicInteger visitors = new AtomicInteger(0);
    private Long id;
    private String title;
    private String content;
    private List<Long> boardImageIds = new ArrayList<>();
    private Long userId;

    public Board(String title, String content, List<Long> boardImageIds, Long userId) {
        this.title = title;
        this.content = content;
        this.boardImageIds = boardImageIds;
        this.userId = userId;
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

    public void setId(Long id) {
        this.id = id;
    }
}
