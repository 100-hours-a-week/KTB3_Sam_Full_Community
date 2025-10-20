package com.example.community.controller;

import com.example.community.auth.JwtUtil;
import com.example.community.common.SuccessCode;
import com.example.community.dto.response.APIResponse;
import com.example.community.dto.response.LikePostResponse;
import com.example.community.entity.Like;
import com.example.community.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    public ResponseEntity<APIResponse<LikePostResponse>> postLike(HttpServletRequest servletRequest, @PathVariable("boardId") Long boardId) {
        Long userId = jwtUtil.extractUserId((String) servletRequest.getAttribute("accessToken"));
        Like like = likeService.postLike(userId, boardId);
        return ResponseEntity.ok(APIResponse.success(SuccessCode.BOARD_LIKED, LikePostResponse.from(like)));
    }

    @DeleteMapping("/boards/{boardId}/like")
    public ResponseEntity<APIResponse<Void>> deleteLike(HttpServletRequest servletRequest, @PathVariable("boardId") Long boardId) {
        Long userId = jwtUtil.extractUserId((String) servletRequest.getAttribute("accessToken"));
        likeService.deleteLike(userId, boardId);
        return ResponseEntity.ok(APIResponse.success(SuccessCode.BOARD_LIKE_CANCELLED, null));
    }
}
