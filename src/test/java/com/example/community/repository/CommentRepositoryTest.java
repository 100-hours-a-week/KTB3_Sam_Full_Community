package com.example.community.repository;

import com.example.community.QueryDslTestConfig;
import com.example.community.entity.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QueryDslTestConfig.class)
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    EntityManager em;

    @Test
    void findAllByBoardId() {
        //given
        User user1 = saveUser("user1@test.com");
        User user2 = saveUser("user2@test.com");

        Image image1 = saveImage();
        Image image2 = saveImage();

        UserImage userImage1 = saveUserImage(user1, image1);
        UserImage userImage2 = saveUserImage(user2, image2);

        Board board = saveBoard(user1, "title");

        saveComment(user1, board, "comment1");
        saveComment(user2, board, "comment2");

        em.flush();
        em.clear();


        //when
        List<Comment> result = commentRepository.findAllByBoardId(board.getId());


        //then
        assertThat(result).hasSize(2);
    }

    @Test
    void findAllByBoardIds() {
        //given
        User user1 = saveUser("user1@test.com");
        User user2 = saveUser("user2@test.com");

        Board board1 = saveBoard(user1, "title1");
        Board board2 = saveBoard(user1, "title2");

        saveComment(user1, board1, "comment1");
        saveComment(user2, board1, "comment2");
        saveComment(user1, board2, "comment3");

        em.flush();
        em.clear();

        List<Long> boardIds = List.of(board1.getId(), board2.getId());


        //when
        List<Comment> result = commentRepository.findAllByBoardId(boardIds);


        //then
        assertThat(result).hasSize(3);
    }

    @Test
    void deleteByBoardId() {
        //given
        User user = saveUser("user@test.com");
        Board board = saveBoard(user, "title");

        saveComment(user, board, "comment1");
        saveComment(user, board, "comment2");

        em.flush();
        em.clear();


        //when
        commentRepository.deleteByBoardId(board.getId());
        em.flush();
        em.clear();


        //then
        List<Comment> result = commentRepository.findAllByBoardId(board.getId());
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

    private Comment saveComment(User user, Board board, String content) {
        Comment comment = new Comment(user, board, content);
        em.persist(comment);
        return comment;
    }

    private Image saveImage() {
        Image image = new Image();
        em.persist(image);
        return image;
    }

    private UserImage saveUserImage(User user, Image image) {
        UserImage userImage = new UserImage(user, image);
        em.persist(userImage);
        return userImage;
    }
}