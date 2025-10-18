package com.example.community.service;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.dto.PagedData;
import com.example.community.entity.Comment;
import com.example.community.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    public Comment postComment(Long userId, Long boardId, String content) {
        return commentRepository.save(new Comment(userId, boardId, content));
    }

    public List<Comment> findAllByBoardIds(List<Long> boardIds) {
        return commentRepository.findAllByBoardIds(boardIds);
    }

    public List<Comment> findAllByBoardId(Long boardId) {
        return commentRepository.findAllByBoardId(boardId);
    }

    public List<Comment> findPageByBoardId(Long boardId, int page, int size) {
        return commentRepository.findPageByBoardId(boardId, page,size);
    }

    public int count() {
        return commentRepository.count();
    }

    public void deleteByBoardId(Long boardId) {
        commentRepository.deleteByBoardId(boardId);
    }

    @Transactional
    public void updateComment(Long userId, Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_COMMENT));
        validateUser(comment, userId);

        comment.updateComment(content);
        comment.recordModificationTime();
        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_COMMENT));
        validateUser(comment, userId);

        commentRepository.deleteById(commentId);
    }

    private void validateUser(Comment comment, Long userId) {
        if(!comment.getUserId().equals(userId)) {
            throw new BaseException(ErrorCode.INVALID_REQUEST);
        }
    }
}
