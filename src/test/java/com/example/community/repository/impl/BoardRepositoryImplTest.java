package com.example.community.repository.impl;

import com.example.community.QueryDslTestConfig;
import com.example.community.entity.Board;
import com.example.community.entity.Image;
import com.example.community.entity.User;
import com.example.community.entity.UserImage;
import com.example.community.repository.BoardRepository;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.DESC;

@DataJpaTest
@Import(QueryDslTestConfig.class)
@EnableJpaAuditing
class BoardRepositoryImplTest {
    @Autowired
    BoardRepository boardRepository;

    @Autowired
    EntityManager em;

    @Test
    void findAllByTitleAndContent() {
        //given
        User user = saveUser("user@test.com");

        Board board1 = saveBoard(user, "Hello Title", "This is Content");
        Board board2 = saveBoard(user, "Another Hello", "Content matches keyword");
        Board board3 = saveBoard(user, "No match title", "irrelevant");

        em.flush();
        em.clear();

        Pageable pageable = PageRequest.of(0, 10, Sort.by(DESC, "updatedAt"));


        //when
        Page<Board> result = boardRepository.findAll("Hello", "Content", pageable);


        //then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).getUpdatedAt())
                .isAfterOrEqualTo(result.getContent().get(1).getUpdatedAt());
        assertThat(result.getContent().get(0).getUser().getUserImage().getImage()).isNotNull();
    }

    @Test
    void findAllByTitleAndContentNull() {
        //given
        User user = saveUser("user@test.com");

        saveBoard(user, "Hello Title", "AAA");
        saveBoard(user, "Hello Again", "BBB");
        saveBoard(user, "NoMatch", "CCC");

        em.flush();
        em.clear();

        Pageable pageable = PageRequest.of(0, 10, Sort.by(DESC, "updatedAt"));


        //when
        Page<Board> result = boardRepository.findAll("Hello", null, pageable);


        //then
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @Test
    void findAllByTitleAndContentBlank() {
        //given
        User user = saveUser("user@test.com");

        saveBoard(user, "Hello Title", "AAA");
        saveBoard(user, "Hello Again", "BBB");
        saveBoard(user, "NoMatch", "CCC");

        em.flush();
        em.clear();

        Pageable pageable = PageRequest.of(0, 10, Sort.by(DESC, "updatedAt"));


        //when
        Page<Board> result = boardRepository.findAll("Hello", " ", pageable);


        //then
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @Test
    void findAllByContentAndTitleNull() {
        //given
        User user = saveUser("user@test.com");

        saveBoard(user, "AAA", "Match Content");
        saveBoard(user, "BBB", "Another Content Match");
        saveBoard(user, "CCC", "irrelevant");

        em.flush();
        em.clear();

        Pageable pageable = PageRequest.of(0, 10, Sort.by(DESC, "updatedAt"));


        //when
        Page<Board> result = boardRepository.findAll(null, "Content", pageable);


        //then
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @Test
    void findAllByContentAndTitleBlank() {
        //given
        User user = saveUser("user@test.com");

        saveBoard(user, "AAA", "Match Content");
        saveBoard(user, "BBB", "Another Content Match");
        saveBoard(user, "CCC", "irrelevant");

        em.flush();
        em.clear();

        Pageable pageable = PageRequest.of(0, 10, Sort.by(DESC, "updatedAt"));


        //when
        Page<Board> result = boardRepository.findAll(" ", "Content", pageable);


        //then
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    private User saveUser(String email) {
        User user = new User(email, "password", "nickname");
        em.persist(user);

        Image image = new Image();
        em.persist(image);

        UserImage userImage = new UserImage(user, image);
        em.persist(userImage);

        return user;
    }

    private Board saveBoard(User user, String title, String content) {
        Board board = new Board(title, content, user);
        em.persist(board);
        return board;
    }
}