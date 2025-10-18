package com.example.community.service;

import com.example.community.entity.Comment;
import com.example.community.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment postComment(Long userId, Long boardId, String content) {
        return commentRepository.save(new Comment(userId, boardId, content));
    }

    public List<Comment> findAllByBoardIds(List<Long> boardIds) {
        return commentRepository.findAllByBoardIds(boardIds);
    }

    public List<Comment> findAllByBoardId(Long boardId) {
        return commentRepository.findAllByBoardId(boardId);
    }

    public void deleteByBoardId(Long boardId) {
        commentRepository.deleteByBoardId(boardId);
    }
}
