package com.example.community.controller;

import com.example.community.auth.JwtUtil;
import com.example.community.common.SuccessCode;
import com.example.community.dto.request.CommentPostRequest;
import com.example.community.dto.response.ApiResponse;
import com.example.community.dto.response.CommentPostResponse;
import com.example.community.entity.Comment;
import com.example.community.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {
    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    CommentController(CommentService commentService, JwtUtil jwtUtil) {
        this.commentService = commentService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/boards/{boardId}/comments")
    public ResponseEntity<ApiResponse<CommentPostResponse>> postComment(HttpServletRequest servletRequest, @PathVariable Long boardId, @Valid @RequestBody CommentPostRequest request) {
        Long userId = jwtUtil.extractUserId((String) servletRequest.getAttribute("accessToken"));
        Comment comment = commentService.postComment(userId, boardId, request.content());
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.COMMENT_CREATED, CommentPostResponse.from(comment)));
    }
}
