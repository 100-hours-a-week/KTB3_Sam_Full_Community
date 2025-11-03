package com.example.community.facade;

import com.example.community.dto.PageInfo;
import com.example.community.dto.PagedData;
import com.example.community.dto.response.CommentInfoResponse;
import com.example.community.entity.Board;
import com.example.community.entity.Comment;
import com.example.community.service.BoardService;
import com.example.community.service.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentQueryFacade {
    private final CommentService commentService;
    private final BoardService boardService;

    CommentQueryFacade(CommentService commentService, BoardService boardService) {
        this.commentService = commentService;
        this.boardService = boardService;
    }

    @Transactional(readOnly = true)
    public PagedData getCommentsPageByBoardId(Long boardId, int page, int size) {
        Board board = boardService.findById(boardId);
        List<CommentInfoResponse> commentInfoResponses = commentService.findPageByBoard(board, page,size).stream()
                .map(CommentInfoResponse::from)
                .toList();

        int totalElements = commentService.count();
        PageInfo pageInfo = PageInfo.from(commentInfoResponses, totalElements, page,size);
        return new PagedData(commentInfoResponses, pageInfo);
    }
}
