package com.example.community.repository;

import com.example.community.entity.Comment;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;

@Repository
public class CommentRepository {
    private Map<Long, Comment> commentDB;
    private long sequence = 0L;

    CommentRepository() {
        this.commentDB = new LinkedHashMap<>();
    }
}
