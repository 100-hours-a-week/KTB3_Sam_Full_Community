package com.example.community.controller;

import com.example.community.auth.JwtUtil;
import com.example.community.common.SuccessCode;
import com.example.community.dto.response.APIResponse;
import com.example.community.dto.response.LikePostResponse;
import com.example.community.entity.Like;
import com.example.community.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "좋아요 API", description = "좋아요 추가,삭제 API")
@RestController
public class LikeController {
    private final LikeService likeService;
    private final JwtUtil jwtUtil;

    LikeController(LikeService likeService, JwtUtil jwtUtil) {
        this.likeService = likeService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "좋아요 추가", description = "입력받은 유저 ID와 게시글 ID에 해당하는 좋아요를 생성합니다.")
    @PostMapping("/boards/{boardId}/like")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "board_like_success"),
            @ApiResponse(responseCode = "404", description = "not_found_board"),
            @ApiResponse(responseCode = "400", description = "invalid_request"),
            @ApiResponse(responseCode = "500", description = "internal_server_error")
    })
    public ResponseEntity<APIResponse<LikePostResponse>> postLike(HttpServletRequest servletRequest,
                                                                  @Parameter(description = "좋아요 추가할 게시글 ID", required = true, example = "3")
                                                                  @PathVariable("boardId") Long boardId) {
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
