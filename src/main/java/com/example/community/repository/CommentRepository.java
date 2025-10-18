package com.example.community.repository;

import com.example.community.entity.Board;
import com.example.community.entity.Comment;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CommentRepository {
    private Map<Long, Comment> commentDB;
    private long sequence = 0L;

    CommentRepository() {
        this.commentDB = new LinkedHashMap<>();
    }

    public Comment save(Comment comment) {
        if(comment.getId() == null) {
            comment.setId(++sequence);
        }
        commentDB.put(comment.getId(), comment);
        return comment;
    }

    public List<Comment> findAll() {
        return new ArrayList<>(commentDB.values());
    }

    public Optional<Comment> findById(Long id) {
        return Optional.ofNullable(commentDB.get(id));
    }

    public void deleteById(Long id) {
        commentDB.remove(id);
    }

    public List<Comment> findAllByBoardId(Long boardId) {
        return commentDB.values().stream()
                .filter(comment -> comment.getBoardId().equals(boardId))
                .toList();
    }

    public List<Comment> findAllByBoardIds(List<Long> boardIds) {
        return commentDB.values().stream()
                .filter(comment -> boardIds.contains(comment.getBoardId()))
                .toList();
    }
}
