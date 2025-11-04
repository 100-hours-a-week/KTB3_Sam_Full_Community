package com.example.community.facade;

import com.example.community.dto.PageInfo;
import com.example.community.dto.PagedData;
import com.example.community.dto.response.BoardInfoResponse;
import com.example.community.entity.Board;
import com.example.community.entity.Comment;
import com.example.community.entity.Like;
import com.example.community.service.BoardService;
import com.example.community.service.CommentService;
import com.example.community.service.LikeService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BoardQueryFacade {
    private final BoardService boardService;
    private final CommentService commentService;
    private final LikeService likeService;

    BoardQueryFacade(BoardService boardService, CommentService commentService, LikeService likeService) {
        this.boardService = boardService;
        this.commentService = commentService;
        this.likeService = likeService;
    }

    @Transactional(readOnly = true)
    public PagedData getAllPagedBoards(int page, int size) {
        Page<Board> boards = boardService.findPage(page,size);

        List<Long> boardIds = boards.stream().map(Board::getId).toList();

        Map<Long, List<Like>> likeMap = likeService.findAllByPagedBoardIds(boardIds)
                .stream()
                .collect(Collectors.groupingBy(Like::getBoardId));

        Map<Long, List<Comment>> commentMap = commentService.findAllByPagedBoardIds(boardIds)
                .stream()
                .collect(Collectors.groupingBy(Comment::getBoardId));


        List<BoardInfoResponse> responses = boards.stream()
                .map(board -> BoardInfoResponse.of(
                        board,
                        likeMap.getOrDefault(board.getId(), List.of()).size(),
                        board.getVisitors(),
                        commentMap.getOrDefault(board.getId(), List.of()).size(),
                        board.getUser()
                )).toList();

        return new PagedData(responses,PageInfo.from(boards));
    }

    @Transactional(readOnly = true)
    public BoardInfoResponse getBoardDetail(Long boardId) {
        Board board = boardService.findById(boardId);
        List<Comment> comments = commentService.findAllByBoardId(boardId);
        List<Like> likes = likeService.findAllByBoard(boardId);

        return BoardInfoResponse.of(board, comments.size(),board.recordVisit(), likes.size(), board.getUser());
    }
}
