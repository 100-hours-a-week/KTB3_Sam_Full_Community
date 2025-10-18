package com.example.community.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
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

    public int recordVisite() {
        return this.visitors.incrementAndGet();
    }

    public int getVisitors() {
        return visitors.get();
    }
}
