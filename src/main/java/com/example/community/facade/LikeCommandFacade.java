package com.example.community.facade;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.Board;
import com.example.community.entity.Like;
import com.example.community.entity.User;
import com.example.community.service.BoardService;
import com.example.community.service.LikeService;
import com.example.community.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeCommandFacade {
    private LikeService likeService;
    private UserService userService;
    private BoardService boardService;

    LikeCommandFacade(LikeService likeService, UserService userService, BoardService boardService) {
        this.likeService = likeService;
        this.userService = userService;
        this.boardService = boardService;
    }

    @Transactional
    public Like postLike(Long userId, Long boardId) {
        User user = userService.getUser(userId);
        Board board = boardService.findById(boardId);
        if(likeService.findByUserIdAndBoardId(userId, boardId).isPresent()) {
            throw new BaseException(ErrorCode.ALREADY_LIKED_POST);
        }
        return likeService.save(user, board);
    }
}
