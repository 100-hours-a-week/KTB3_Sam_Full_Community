package com.example.community.controller;

import com.example.community.auth.JwtUtil;
import com.example.community.common.SuccessCode;
import com.example.community.dto.request.PostBoardRequest;
import com.example.community.dto.response.ApiResponse;
import com.example.community.dto.response.BoardPostResponse;
import com.example.community.entity.Board;
import com.example.community.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoardController {
    private final BoardService boardService;
    private final JwtUtil jwtUtil;

    BoardController(BoardService boardService, JwtUtil jwtUtil) {
        this.boardService = boardService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/boards")
    public ResponseEntity<ApiResponse<BoardPostResponse>> postBoard(HttpServletRequest servletRequest, @Valid @RequestBody PostBoardRequest request) {
        Long userId = jwtUtil.extractUserId((String) servletRequest.getAttribute("accessToken"));
        Board board = boardService.post(userId, request.title(), request.content(), request.boardImageIds());
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.BOARD_CREATED, BoardPostResponse.from(board)));
    }


}
