package com.example.community.controller;

import com.example.community.auth.JwtUtil;
import com.example.community.common.SuccessCode;
import com.example.community.dto.request.CommentModifyRequest;
import com.example.community.dto.request.CommentPostRequest;
import com.example.community.dto.response.ApiResponse;
import com.example.community.dto.response.CommentInfoResponse;
import com.example.community.dto.response.CommentPostResponse;
import com.example.community.dto.response.PageApiResponse;
import com.example.community.entity.Comment;
import com.example.community.facade.CommentQueryFacade;
import com.example.community.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentController {
    private final CommentQueryFacade commentQueryFacade;
    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    CommentController(CommentQueryFacade commentQueryFacade, CommentService commentService, JwtUtil jwtUtil) {
        this.commentQueryFacade = commentQueryFacade;
        this.commentService = commentService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/boards/{boardId}/comments")
    public ResponseEntity<ApiResponse<CommentPostResponse>> postComment(HttpServletRequest servletRequest, @PathVariable("boardId") Long boardId, @Valid @RequestBody CommentPostRequest request) {
        Long userId = jwtUtil.extractUserId((String) servletRequest.getAttribute("accessToken"));
        Comment comment = commentService.postComment(userId, boardId, request.content());
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.COMMENT_CREATED, CommentPostResponse.from(comment)));
    }

    @GetMapping("/boards/{boardId}/comments")
    public ResponseEntity<PageApiResponse<CommentInfoResponse>> getComments(@PathVariable("boardId") Long boardId,
                                                                            @RequestParam(defaultValue = "1") int page,
                                                                            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(PageApiResponse.success(SuccessCode.ALL_COMMENTS_ON_BOARD_FOUND, commentQueryFacade.getCommentsPageByBoardId(boardId, page, size)));
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<ApiResponse<Void>> modifyComment(HttpServletRequest servletRequest, @PathVariable("id") Long id, @Valid @RequestBody CommentModifyRequest request) {
        Long userId = jwtUtil.extractUserId((String) servletRequest.getAttribute("accessToken"));
        commentService.updateComment(userId, id, request.content());
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.COMMENT_UPDATED, null));
    }
}
