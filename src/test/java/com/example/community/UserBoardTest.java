package com.example.community;

import com.example.community.entity.Board;
import com.example.community.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.query.hql.spi.SemanticPathPart;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class UserBoardTest {
    @PersistenceContext
    EntityManager em;

    @Test
    void userPostingTest() {
        User user = new User("test@test.co.kr", "testPassword", "testNickname", 3L);
        em.persist(user);

        List<Long> boardIds = List.of(1L,2L,3L);
        Board newUserBoard = new Board("testBoardTitle", "testBoardContent", boardIds, user);
        em.persist(newUserBoard);

        Board foundBoard = em.find(Board.class, 1L);
        System.out.println("foundBoardTitle : " +foundBoard.getTitle());
    }
}
