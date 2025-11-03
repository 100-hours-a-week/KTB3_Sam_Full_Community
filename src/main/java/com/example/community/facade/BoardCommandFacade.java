package com.example.community.facade;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.Board;
import com.example.community.entity.User;
import com.example.community.service.BoardService;
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
    public void update(Long userId, Long boardId, String title, String content, List<Long> boardImageIds) {
        Board board = boardService.findById(boardId);
        validateUser(board, userId);
        boardService.updateById(boardId,title,content, boardImageIds);
    }

    @Transactional
    public void delete(Long userId, Long boardId) {
        Board board = boardService.findById(boardId);
        validateUser(board, userId);
        boardService.deleteById(boardId);
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        User user = userService.getUser(userId);
        boardService.deleteByUser(user);
    }

    private void validateUser(Board board, Long userId) {
        if(!board.getAuthor().getId().equals(userId)) {
            throw new BaseException(ErrorCode.INVALID_REQUEST);
        }
    }
}
