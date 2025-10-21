package com.example.community.controller;

import com.example.community.auth.JwtUtil;
import com.example.community.common.SuccessCode;
import com.example.community.dto.request.CommentModifyRequest;
import com.example.community.dto.request.CommentPostRequest;
import com.example.community.dto.response.APIResponse;
import com.example.community.dto.response.CommentInfoResponse;
import com.example.community.dto.response.CommentPostResponse;
import com.example.community.dto.response.PageApiResponse;
import com.example.community.entity.Comment;
import com.example.community.facade.CommentQueryFacade;
import com.example.community.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "댓글 API", description = "댓글 관련 API")
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

    @Operation(summary = "댓글 생성", description = "게시글 ID를 입력받아 해당 게시글에 댓글을 추가합니다.")
    @PostMapping("/boards/{boardId}/comments")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "comment_upload_success"),
            @ApiResponse(responseCode = "400", description = "invalid_request"),
            @ApiResponse(responseCode = "500", description = "internal_server_error")
    })
    public ResponseEntity<APIResponse<CommentPostResponse>> postComment(HttpServletRequest servletRequest,
                                                                        @Parameter(description = "댓글 추가할 게시글 ID", required = true, example = "1")
                                                                        @PathVariable("boardId") Long boardId,
                                                                        @Valid @RequestBody CommentPostRequest request) {
        Long userId = jwtUtil.extractUserId((String) servletRequest.getAttribute("accessToken"));
        Comment comment = commentService.postComment(userId, boardId, request.content());
        return ResponseEntity.ok(APIResponse.success(SuccessCode.COMMENT_CREATED, CommentPostResponse.from(comment)));
    }

    @Operation(summary = "댓글 조회", description = "원하는 페이지 위치와 한 페이지당 크기와 함께 댓글 조회를 원하는 게시글 ID를 함께 받아 해당하는 댓글 내용을 반환합니다.")
    @GetMapping("/boards/{boardId}/comments")
    @SecurityRequirement(name="JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "find_all_comments_on_board_success"),
            @ApiResponse(responseCode = "500", description = "internal_server_error")
    })
    public ResponseEntity<PageApiResponse<CommentInfoResponse>> getComments(@Parameter(description = "댓글 조회 진행할 게시글 ID", required = true, example = "2")
                                                                            @PathVariable("boardId") Long boardId,
                                                                            @RequestParam(defaultValue = "1") int page,
                                                                            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(PageApiResponse.success(SuccessCode.ALL_COMMENTS_ON_BOARD_FOUND, commentQueryFacade.getCommentsPageByBoardId(boardId, page, size)));
    }

    @Operation(summary = "댓글 수정", description = "댓글 수정을 진행합니다.")
    @PutMapping("/comments/{id}")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "comment_update_success"),
            @ApiResponse(responseCode = "400", description = "invalid_request"),
            @ApiResponse(responseCode = "404", description = "not_found_comment"),
            @ApiResponse(responseCode = "500", description = "internal_server_error")
    })
    public ResponseEntity<APIResponse<Void>> modifyComment(HttpServletRequest servletRequest,
                                                           @Parameter(description = "수정할 댓글 ID", required = true, example = "3")
                                                           @PathVariable("id") Long id,
                                                           @Valid @RequestBody CommentModifyRequest request) {
        Long userId = jwtUtil.extractUserId((String) servletRequest.getAttribute("accessToken"));
        commentService.updateComment(userId, id, request.content());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "댓글 삭제", description = "입력받은 ID에 해당하는 댓글을 삭제합니다.")
    @DeleteMapping("/comments/{id}")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "comment_delete_success"),
            @ApiResponse(responseCode = "404", description = "already_deleted_comment"),
            @ApiResponse(responseCode = "500", description = "internal_server_error")
    })
    public ResponseEntity<APIResponse<Void>> deleteComment(HttpServletRequest servletRequest,
                                                            @Parameter(description = "삭제할 댓글 ID", required = true, example = "3")
                                                            @PathVariable("id") Long id) {
        Long userId = jwtUtil.extractUserId((String) servletRequest.getAttribute("accessToken"));
        commentService.deleteComment(userId, id);
        return ResponseEntity.noContent().build();
    }
}
