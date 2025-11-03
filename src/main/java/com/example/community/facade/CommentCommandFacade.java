package com.example.community.facade;

import com.example.community.entity.Board;
import com.example.community.entity.Comment;
import com.example.community.entity.User;
import com.example.community.service.BoardService;
import com.example.community.service.CommentService;
import com.example.community.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentCommandFacade {
    private final CommentService commentService;
    private final UserService userService;
    private final BoardService boardService;

    CommentCommandFacade(CommentService commentService, UserService userService, BoardService boardService) {
        this.commentService = commentService;
        this.userService = userService;
        this.boardService = boardService;
    }

    @Transactional
    public Comment postComment(Long userId, Long boardId, String content) {
        User user = userService.getUser(userId);
        Board board = boardService.findById(boardId);
        return commentService.save(user,board, content);
    }

    @Transactional
    public void deleteByBoardId(Long boardId) {
        Board board = boardService.findById(boardId);
        commentService.deleteByBoard(board);
    }
}
