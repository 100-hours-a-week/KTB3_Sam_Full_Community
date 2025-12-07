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

@DataJpaTest
@Import(QueryDslTestConfig.class)
class BoardImageRepositoryTest {
    @Autowired
    private BoardImageRepository boardImageRepository;

    @Autowired
    private EntityManager em;

    @Test
    void findByBoardIdAndImageId() {
        //given
        User user = saveUser("user@test.com");
        Board board = saveBoard(user, "title");
        Image image = saveImage();
        BoardImage boardImage = saveBoardImage(board, image);

        em.flush();
        em.clear();


        //when
        Optional<BoardImage> result =
                boardImageRepository.findByBoardIdAndImageId(board.getId(), image.getId());


        //then
        assertThat(result).isPresent();
        assertThat(result.get().getBoard().getId()).isEqualTo(board.getId());
        assertThat(result.get().getImage().getId()).isEqualTo(image.getId());
    }

    @Test
    void findByBoardId() {
        //given
        User user = saveUser("user@test.com");

        Board board = saveBoard(user, "title");
        Image image1 = saveImage();
        Image image2 = saveImage();

        saveBoardImage(board, image1);
        saveBoardImage(board, image2);

        em.flush();
        em.clear();


        //when
        List<BoardImage> results = boardImageRepository.findByBoardId(board.getId());


        //then
        assertThat(results).hasSize(2);
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

    private Image saveImage() {
        Image image = new Image();
        em.persist(image);
        return image;
    }

    private BoardImage saveBoardImage(Board board, Image image) {
        BoardImage boardImage = new BoardImage(board, image);
        em.persist(boardImage);
        return boardImage;
    }
}