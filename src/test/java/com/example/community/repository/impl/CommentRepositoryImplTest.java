package com.example.community.repository.impl;

import com.example.community.QueryDslTestConfig;
import com.example.community.entity.*;
import com.example.community.repository.CommentRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.DESC;

@DataJpaTest
@Import(QueryDslTestConfig.class)
@EnableJpaAuditing
class CommentRepositoryImplTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EntityManager em;

    @Test
    void findAllByBoardIdPageable() {
        // given
        User user = saveUser("user@test.com");
        Board board = saveBoard(user, "title");

        Comment comment1 = saveComment(user, board, "content1");
        Comment comment2 = saveComment(user, board, "content2");
        Comment comment3 = saveComment(user, board, "content3");

        em.flush();
        em.clear();

        Pageable pageable = PageRequest.of(0, 2, Sort.by(DESC, "updatedAt"));


        // when
        Page<Comment> result = commentRepository.findAllByBoardId(board.getId(), pageable);


        // then
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getUpdatedAt())
                .isAfterOrEqualTo(result.getContent().get(1).getUpdatedAt());
    }


    private User saveUser(String email) {
        User user = new User(email, "password", "nickname");
        em.persist(user);
        return user;
    }

    private Board saveBoard(User user, String title) {
        Board board = new Board(title, "content", user);
        em.persist(board);
        return board;
    }

    private Comment saveComment(User user, Board board, String content) {
        Comment comment = new Comment(user, board, content);
        em.persist(comment);
        return comment;
    }
}