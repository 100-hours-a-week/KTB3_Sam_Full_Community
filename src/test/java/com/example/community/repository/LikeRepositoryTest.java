package com.example.community.repository;

import com.example.community.QueryDslTestConfig;
import com.example.community.entity.Board;
import com.example.community.entity.Like;
import com.example.community.entity.User;
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
class LikeRepositoryTest {
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private EntityManager em;

    @Test
    void findByUserIdAndBoardId() {
        //given
        User user = saveUser("email");
        Board board = saveBoard(user, "title");
        saveLike(user, board);
        em.flush();
        em.clear();


        //when
        Optional<Like> result = likeRepository.findByUserIdAndBoardId(user.getId(), board.getId());


        //then
        assertThat(result).isPresent();
        assertThat(result.get().getUser().getId()).isEqualTo(user.getId());
        assertThat(result.get().getBoard().getId()).isEqualTo(board.getId());
    }

    @Test
    void findAllByBoardIds() {
        //given
        User user1 = saveUser("user1@test.com");
        User user2 = saveUser("user2@test.com");
        User user3 = saveUser("user3@test.com");

        Board board1 = saveBoard(user1, "title1");
        Board board2 = saveBoard(user2, "title2");

        saveLike(user1, board2);
        saveLike(user2, board1);
        saveLike(user3, board1);
        saveLike(user3, board2);

        em.flush();
        em.clear();


        //when
        List<Like> result = likeRepository.findAllByBoardIds(List.of(board1.getId(), board2.getId()));


        //then
        assertThat(result).hasSize(4);
    }

    @Test
    void findAllByBoardId() {
        //given
        User user1 = saveUser("user1@test.com");
        User user2 = saveUser("user2@test.com");
        User user3 = saveUser("user3@test.com");

        Board board = saveBoard(user1, "title");

        saveLike(user1, board);
        saveLike(user2, board);
        saveLike(user3, board);

        em.flush();
        em.clear();


        //when
        List<Like> result = likeRepository.findAllByBoardId(board.getId());


        //then
        assertThat(result).hasSize(3);
    }

    @Test
    void deleteByBoardId() {
        //given
        User user = saveUser("email@test.com");
        Board board = saveBoard(user, "title");

        saveLike(user, board);

        em.flush();
        em.clear();


        //when
        likeRepository.deleteByBoardId(board.getId());
        em.flush();
        em.clear();


        //then
        List<Like> result = likeRepository.findAllByBoardId(board.getId());
        assertThat(result).isEmpty();
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

    private Like saveLike(User user, Board board) {
        Like like = new Like(user, board);
        em.persist(like);
        return like;
    }
}