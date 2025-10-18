package com.example.community.controller;

import com.example.community.auth.JwtUtil;
import com.example.community.common.SuccessCode;
import com.example.community.dto.response.ApiResponse;
import com.example.community.dto.response.LikePostResponse;
import com.example.community.entity.Like;
import com.example.community.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LikeController {
    private final LikeService likeService;
    private final JwtUtil jwtUtil;

    LikeController(LikeService likeService, JwtUtil jwtUtil) {
        this.likeService = likeService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/boards/{boardId}/like")
    public ResponseEntity<ApiResponse<LikePostResponse>> postLike(HttpServletRequest servletRequest, @PathVariable("boardId") Long boardId) {
        Long userId = jwtUtil.extractUserId((String) servletRequest.getAttribute("accessToken"));
        Like like = likeService.postLike(userId, boardId);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.BOARD_LIKED, LikePostResponse.from(like)));
    }
}
