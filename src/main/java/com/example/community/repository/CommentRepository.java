package com.example.community.repository;

import com.example.community.entity.Comment;
import com.example.community.repository.interfaces.CommentCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository {
}
