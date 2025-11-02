package com.example.community.service;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.Board;
import com.example.community.entity.Comment;
import com.example.community.entity.User;
import com.example.community.repository.inmemory.InMemoryCommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    private final InMemoryCommentRepository inMemoryCommentRepository;

    CommentService(InMemoryCommentRepository inMemoryCommentRepository) {
        this.inMemoryCommentRepository = inMemoryCommentRepository;
    }

    public Comment save(User user, Board board, String content) {
        return inMemoryCommentRepository.save(new Comment(user,board, content));
    }

    public List<Comment> findAllByBoardIds(List<Long> boardIds) {
        return inMemoryCommentRepository.findAllByBoardIds(boardIds);
    }

    public List<Comment> findAllByBoardId(Long boardId) {
        return inMemoryCommentRepository.findAllByBoardId(boardId);
    }

    public List<Comment> findPageByBoardId(Long boardId, int page, int size) {
        return inMemoryCommentRepository.findPageByBoardId(boardId, page,size);
    }

    public int count() {
        return inMemoryCommentRepository.count();
    }

    public void deleteByBoardId(Long boardId) {
        inMemoryCommentRepository.deleteByBoardId(boardId);
    }

    @Transactional
    public void updateComment(Long userId, Long commentId, String content) {
        Comment comment = inMemoryCommentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_COMMENT));
        validateUser(comment, userId);

        comment.updateComment(content);
        comment.recordModificationTime();
        inMemoryCommentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = inMemoryCommentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_COMMENT));
        validateUser(comment, userId);

        inMemoryCommentRepository.deleteById(commentId);
    }

    private void validateUser(Comment comment, Long userId) {
        if(!comment.getUser().getId().equals(userId)) {
            throw new BaseException(ErrorCode.INVALID_REQUEST);
        }
    }
}
