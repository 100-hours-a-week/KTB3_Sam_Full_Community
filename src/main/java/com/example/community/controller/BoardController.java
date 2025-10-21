package com.example.community.controller;

import com.example.community.auth.JwtUtil;
import com.example.community.common.SuccessCode;
import com.example.community.dto.PagedData;
import com.example.community.dto.request.BoardPostRequest;
import com.example.community.dto.request.BoardUpdateRequest;
import com.example.community.dto.response.APIResponse;
import com.example.community.dto.response.BoardInfoResponse;
import com.example.community.dto.response.BoardPostResponse;
import com.example.community.dto.response.PageApiResponse;
import com.example.community.entity.Board;
import com.example.community.facade.BoardCommandFacade;
import com.example.community.facade.BoardQueryFacade;
import com.example.community.service.BoardService;
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

import java.util.List;

@Tag(name = "게시글 API", description = "게시글 관련 API")
@RestController
public class BoardController {
    private final BoardCommandFacade boardCommandFacade;
    private final BoardQueryFacade boardQueryFacade;
    private final BoardService boardService;
    private final JwtUtil jwtUtil;

    BoardController(BoardService boardService, JwtUtil jwtUtil, BoardCommandFacade boardCommandFacade, BoardQueryFacade boardQueryFacade) {
        this.boardService = boardService;
        this.jwtUtil = jwtUtil;
        this.boardCommandFacade = boardCommandFacade;
        this.boardQueryFacade = boardQueryFacade;
    }

    @Operation(summary = "게시글 추가", description = "입력받은 내용을 바탕으로 새로운 게시글을 추가합니다.")
    @PostMapping("/boards")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "board_create_success"),
            @ApiResponse(responseCode = "400", description = "invalid_request"),
            @ApiResponse(responseCode = "500", description = "internal_server_error")
    })
    public ResponseEntity<APIResponse<BoardPostResponse>> postBoard(HttpServletRequest servletRequest, @Valid @RequestBody BoardPostRequest request) {
        Long userId = jwtUtil.extractUserId((String) servletRequest.getAttribute("accessToken"));
        Board board = boardService.post(userId, request.title(), request.content(), request.boardImageIds());
        return ResponseEntity.ok(APIResponse.success(SuccessCode.BOARD_CREATED, BoardPostResponse.from(board)));
    }

    @Operation(summary = "게시글 전체 조회", description = "원하는 페이지 위치와 한 페이지 당 개수를 입력받아 해당하는 게시글 내용을 조회합니다.")
    @GetMapping("/boards")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "board_find_all_success"),
            @ApiResponse(responseCode = "500", description = "internal_server_error")
    })
    public ResponseEntity<PageApiResponse<List<BoardInfoResponse>>> getBoards (HttpServletRequest servletRequest,
                                                                              @RequestParam(defaultValue = "1") int page,
                                                                              @RequestParam(defaultValue = "10") int size) {
        PagedData pagedBoards = boardQueryFacade.getAllBoards(page,size);
        return ResponseEntity.ok(PageApiResponse.success(SuccessCode.ALL_BOARDS_FOUND, pagedBoards));
    }

    @Operation(summary = "게시글 상세 조회", description = "특정 게시글의 상세 정보를 조회합니다.")
    @GetMapping("/boards/{id}")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "board_detail_find_success"),
            @ApiResponse(responseCode = "400", description = "invalid_request"),
            @ApiResponse(responseCode = "500", description = "internal_server_error")
    })
    public ResponseEntity<APIResponse<BoardInfoResponse>> getBoardDetail(
            @Parameter(description = "게시글 ID", required = true, example = "1")
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(APIResponse.success(SuccessCode.BOARD_DETAIL_FOUND, boardQueryFacade.getBoardDetail(id)));
    }

    @Operation(summary = "게시글 수정", description = "게시글 정보를 수정합니다.")
    @PutMapping("/boards/{id}")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "board_update_success"),
            @ApiResponse(responseCode = "404", description = "not_found_board"),
            @ApiResponse(responseCode = "500", description = "internal_server_error")
    })
    public ResponseEntity<APIResponse<Void>> updateBoard(HttpServletRequest servletRequest,
                                                         @Parameter(description = "게시글 ID", required = true, example = "1")
                                                         @PathVariable("id") Long id,
                                                         @Valid @RequestBody BoardUpdateRequest request) {
        Long userId = jwtUtil.extractUserId((String) servletRequest.getAttribute("accessToken"));
        boardCommandFacade.updateBoard(userId, id, request.title(), request.content(), request.boardImageIds());
        return ResponseEntity.ok(APIResponse.success(SuccessCode.BOARD_UPDATED, null));
    }

    @Operation(summary = "게시글 삭제", description = "게시글 삭제를 진행합니다.")
    @DeleteMapping("/boards/{id}")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "board_delete_success"),
            @ApiResponse(responseCode = "404", description = "already_deleted_board"),
            @ApiResponse(responseCode = "500", description = "internal_server_error")
    })
    public ResponseEntity<APIResponse<Void>> deleteBoard(HttpServletRequest servletRequest,
                                                         @Parameter(description = "게시글 ID", required = true, example = "1")
                                                         @PathVariable("id") Long id) {
        Long userId = jwtUtil.extractUserId((String) servletRequest.getAttribute("accessToken"));
        boardCommandFacade.deleteBoard(userId, id);
        return ResponseEntity.ok(APIResponse.success(SuccessCode.BOARD_DELETED, null));
    }
}
