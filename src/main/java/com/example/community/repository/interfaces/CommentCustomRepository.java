package com.example.community.repository.interfaces;

import com.example.community.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentCustomRepository {
    Page<Comment> findAllByBoardId(Long boardId, Pageable pageable);
}
