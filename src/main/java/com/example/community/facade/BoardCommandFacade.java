package com.example.community.facade;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.Board;
import com.example.community.service.BoardService;
import com.example.community.service.CommentService;
import com.example.community.service.LikeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardCommandFacade {
    private final BoardService boardService;
    private final CommentService commentService;
    private final LikeService likeService;

    BoardCommandFacade(BoardService boardService, CommentService commentService, LikeService likeService) {
        this.boardService = boardService;
        this.commentService = commentService;
        this.likeService = likeService;
    }

    @Transactional
    public void updateBoard(Long userId,Long boardId, String title, String content, List<Long> boardImageIds) {
        Board board = boardService.findById(boardId);
        validateUser(board, userId);
        boardService.updateBoard(boardId,title,content, boardImageIds);
    }

    @Transactional
    public void deleteBoard(Long userId, Long boardId) {
        Board board = boardService.findById(boardId);
        validateUser(board, userId);
        boardService.deleteBoard(boardId);
    }

    private void validateUser(Board board, Long userId) {
        if(!board.getUserId().equals(userId)) {
            throw new BaseException(ErrorCode.INVALID_REQUEST);
        }
    }
}
