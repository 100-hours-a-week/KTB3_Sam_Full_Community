package com.example.community.repository;

import com.example.community.QueryDslTestConfig;
import com.example.community.entity.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QueryDslTestConfig.class)
class BoardRepositoryTest {
    @Autowired
    BoardRepository boardRepository;

    @Autowired
    EntityManager em;

    @Test
    void findById() {
        //given
        User user = saveUser("user@test.com");
        Board board = saveBoard(user, "title");

        em.flush();
        em.clear();


        //when
        Optional<Board> result = boardRepository.findById(board.getId());


        //then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(board.getId());
        assertThat(result.get().getUser().getUserImage().getImage()).isNotNull();
    }

    @Test
    void findByTitle() {
        //given
        User user = saveUser("user@test.com");
        Board board = saveBoard(user, "title");

        em.flush();
        em.clear();


        //when
        Optional<Board> result = boardRepository.findByTitle("title");


        //then
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("title");
    }

    @Test
    void deleteByUserId() {
        //given
        User user = saveUser("user@test.com");
        saveBoard(user, "title1");
        saveBoard(user, "title2");

        em.flush();
        em.clear();


        //when
        boardRepository.deleteByUserId(user.getId());
        em.flush();
        em.clear();


        //then
        List<Board> all = boardRepository.findAll();
        assertThat(all).isEmpty();
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

    private Board saveBoard(User user, String title) {
        Board board = new Board(title, "content", user);
        em.persist(board);
        return board;
    }
}