package com.example.community.service;

import com.example.community.entity.Board;
import com.example.community.entity.BoardImage;
import com.example.community.entity.Image;
import com.example.community.entity.User;
import com.example.community.repository.BoardImageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardImageServiceTest {
    @Mock
    private BoardImageRepository boardImageRepository;

    @InjectMocks
    private BoardImageService boardImageService;


    @Test
    void 게시글_저장시_입력받은_이미지가_이미_존재하는_이미지면_새로저장하지않고_기존값을_반환한다() {
        //given
        User user = new User("email", "password", "nickname");
        ReflectionTestUtils.setField(user, "id", 1L);

        Board board = new Board("title", "content", user);
        ReflectionTestUtils.setField(board, "id", 10L);

        Image image = new Image();
        ReflectionTestUtils.setField(image, "id", 20L);

        BoardImage existing = new BoardImage(board, image);

        given(boardImageRepository.findByBoardIdAndImageId(10L, 20L))
                .willReturn(Optional.of(existing));


        //when
        BoardImage result = boardImageService.save(board, image);


        //then
        assertThat(result).isSameAs(existing);
        then(boardImageRepository).should(never()).save(any());
    }


    @Test
    void 게시글_저장시_입력받은_이미지가_새로운_이미지면_저장후_반환한다() {
        //given
        User user = new User("email", "password", "nickname");
        ReflectionTestUtils.setField(user, "id", 1L);

        Board board = new Board("title", "content", user);
        ReflectionTestUtils.setField(board, "id", 10L);

        Image image = new Image();
        ReflectionTestUtils.setField(image, "id", 20L);

        BoardImage newBoardImage = new BoardImage(board, image);
        ReflectionTestUtils.setField(newBoardImage, "id", 99L);

        given(boardImageRepository.findByBoardIdAndImageId(10L, 20L))
                .willReturn(Optional.empty());

        given(boardImageRepository.save(any(BoardImage.class)))
                .willReturn(newBoardImage);


        //when
        BoardImage result = boardImageService.save(board, image);


        //then
        assertThat(result.getId()).isEqualTo(99L);
        then(boardImageRepository).should(times(1)).save(any(BoardImage.class));
    }


    @Test
    void 게시글_아이디로_게시글_이미지리스트를_조회한다() {
        //given
        User user = new User("email", "password", "nickname");
        ReflectionTestUtils.setField(user, "id", 1L);

        Long boardId = 10L;
        Board board = new Board("title", "content", user);
        ReflectionTestUtils.setField(board, "id", boardId);

        Image image1 = new Image();
        Image image2 = new Image();

        BoardImage boardImage1 = new BoardImage(board, image1);
        BoardImage boardImage2 = new BoardImage(board, image2);

        given(boardImageRepository.findByBoardId(boardId))
                .willReturn(List.of(boardImage1, boardImage2));


        //when
        List<BoardImage> result = boardImageService.findByBoardId(boardId);


        //then
        assertThat(result).hasSize(2);
        then(boardImageRepository).should(times(1)).findByBoardId(boardId);
    }
}