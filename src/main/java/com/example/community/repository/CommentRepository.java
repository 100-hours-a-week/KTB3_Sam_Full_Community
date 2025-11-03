package com.example.community.repository;

import com.example.community.entity.Board;
import com.example.community.entity.Comment;
import com.example.community.repository.interfaces.CommentCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository {
    public List<Comment> findAllByPost(Page<Board> posts);

    public List<Comment> findAllByPost(Board post);

    public void deleteByPost(Board post);

    public Page<Comment> findAllByPost(Board post, Pageable pageable);
}
