package com.example.community.service;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.Board;
import com.example.community.entity.Comment;
import com.example.community.entity.User;
import com.example.community.repository.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment save(User user, Board board, String content) {
        Comment comment = new Comment(user,board, content);
        return commentRepository.save(comment);
    }

    public List<Comment> findAllByPagedBoardIds(List<Long> boardIds) {
        return commentRepository.findAllByBoardId(boardIds);
    }

    public List<Comment> findAllByBoardId(Long boardId) {
        return commentRepository.findAllByBoardId(boardId);
    }

    public Page<Comment> findPageByBoardId(Long boardId, int page, int size) {
        return commentRepository.findAllByBoardId(boardId, PageRequest.of(page-1,size));
    }

    @Transactional
    public void deleteByBoardId(Long boardId) {
        commentRepository.deleteByBoardId(boardId);
    }

    @Transactional
    public void updateById(Long userId, Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_COMMENT));
        validateUser(comment, userId);

        comment.updateComment(content);
        commentRepository.save(comment);
    }

    @Transactional
    public void deleteById(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_COMMENT));
        validateUser(comment, userId);
        commentRepository.deleteById(commentId);
    }

    private void validateUser(Comment comment, Long userId) {
        if(!comment.getUser().getId().equals(userId)) {
            throw new BaseException(ErrorCode.INVALID_REQUEST);
        }
    }
}
