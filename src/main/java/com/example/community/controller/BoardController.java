package com.example.community.controller;

import com.example.community.auth.JwtUtil;
import com.example.community.common.SuccessCode;
import com.example.community.dto.PagedData;
import com.example.community.dto.request.BoardPostRequest;
import com.example.community.dto.request.BoardUpdateRequest;
import com.example.community.dto.response.ApiResponse;
import com.example.community.dto.response.BoardInfoResponse;
import com.example.community.dto.response.BoardPostResponse;
import com.example.community.dto.response.PageApiResponse;
import com.example.community.entity.Board;
import com.example.community.facade.BoardCommandFacade;
import com.example.community.facade.BoardQueryFacade;
import com.example.community.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/boards")
    public ResponseEntity<ApiResponse<BoardPostResponse>> postBoard(HttpServletRequest servletRequest, @Valid @RequestBody BoardPostRequest request) {
        Long userId = jwtUtil.extractUserId((String) servletRequest.getAttribute("accessToken"));
        Board board = boardService.post(userId, request.title(), request.content(), request.boardImageIds());
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.BOARD_CREATED, BoardPostResponse.from(board)));
    }

    @GetMapping("/boards")
    public ResponseEntity<PageApiResponse<List<BoardInfoResponse>>> getBoards (HttpServletRequest servletRequest,
                                                                              @RequestParam(defaultValue = "1") int page,
                                                                              @RequestParam(defaultValue = "10") int size) {
        PagedData pagedBoards = boardQueryFacade.getAllBoards(page,size);
        return ResponseEntity.ok(PageApiResponse.success(SuccessCode.ALL_BOARDS_FOUND, pagedBoards));
    }

    @GetMapping("/boards/{id}")
    public ResponseEntity<ApiResponse<BoardInfoResponse>> getBoardDetail(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.BOARD_DETAIL_FOUND, boardQueryFacade.getBoardDetail(id)));
    }

    @PutMapping("/boards/{id}")
    public ResponseEntity<ApiResponse<Void>> updateBoard(HttpServletRequest servletRequest, @PathVariable Long id, @Valid @RequestBody BoardUpdateRequest request) {
        Long userId = jwtUtil.extractUserId((String) servletRequest.getAttribute("accessToken"));
        boardCommandFacade.updateBoard(userId, id, request.title(), request.content(), request.boardImageIds());
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.BOARD_UPDATED, null));
    }
}
