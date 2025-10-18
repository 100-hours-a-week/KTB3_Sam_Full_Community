package com.example.community.facade;

import com.example.community.dto.PageInfo;
import com.example.community.dto.PagedData;
import com.example.community.dto.response.CommentInfoResponse;
import com.example.community.entity.Comment;
import com.example.community.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentQueryFacade {
    private final CommentService commentService;

    CommentQueryFacade(CommentService commentService) {
        this.commentService = commentService;
    }

    public PagedData getCommentsPageByBoardId(Long boardId, int page, int size) {
        List<CommentInfoResponse> commentInfoResponses = commentService.findPageByBoardId(boardId, page,size).stream()
                .map(CommentInfoResponse::from)
                .toList();

        int totalElements = commentService.count();
        PageInfo pageInfo = PageInfo.from(commentInfoResponses, totalElements, page,size);
        return new PagedData(commentInfoResponses, pageInfo);
    }
}
