package com.example.community.facade;

import com.example.community.dto.PageInfo;
import com.example.community.dto.PagedData;
import com.example.community.dto.response.CommentInfoResponse;
import com.example.community.entity.Board;
import com.example.community.entity.Comment;
import com.example.community.service.BoardService;
import com.example.community.service.CommentService;
import org.springframework.data.domain.Page;
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
    public PagedData getAllPagedCommentsByBoardId(Long boardId, int page, int size) {
        Board board = boardService.findById(boardId);
        Page<Comment> comments = commentService.findPageByBoard(board, page,size);
        List<CommentInfoResponse> commentInfoResponses = comments.stream()
                .map(CommentInfoResponse::from)
                .toList();

        return new PagedData(commentInfoResponses, PageInfo.from(comments));
    }
}
