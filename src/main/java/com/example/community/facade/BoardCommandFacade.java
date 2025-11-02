package com.example.community.facade;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.Board;
import com.example.community.entity.User;
import com.example.community.service.BoardService;
import com.example.community.service.CommentService;
import com.example.community.service.LikeService;
import com.example.community.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardCommandFacade {
    private final BoardService boardService;
    private final UserService userService;

    BoardCommandFacade(BoardService boardService, UserService userService) {
        this.boardService = boardService;
        this.userService = userService;
    }

    @Transactional
    public Board post(Long userId, String title, String content, List<Long> boardImageIds) {
        boardService.validateTitle(title);
        User user = userService.getUser(userId);
        return boardService.save(title, content, boardImageIds, user);
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
