package com.example.community.service;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.Board;
import com.example.community.entity.Comment;
import com.example.community.entity.User;
import com.example.community.repository.CommentRepository;
import com.example.community.repository.inmemory.InMemoryCommentRepository;
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
        board.addComment(comment);
        return commentRepository.save(comment);
    }

    public List<Comment> findAllByPagedBoards(Page<Board> boards) {
        return commentRepository.findAllByPost(boards);
    }

    public List<Comment> findAllByBoard(Board post) {
        return commentRepository.findAllByPost(post);
    }

    public Page<Comment> findPageByBoard(Board board, int page, int size) {
        return commentRepository.findAllByPost(board, PageRequest.of(page,size));
    }

    public int count() {
        return Long.valueOf(commentRepository.count()).intValue();
    }

    public void deleteByBoard(Board board) {
        commentRepository.deleteByPost(board);
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
        if(!comment.getUser().getId().equals(userId)) {
            throw new BaseException(ErrorCode.INVALID_REQUEST);
        }
    }
}
