package com.example.community.facade;

import com.example.community.entity.Board;
import com.example.community.entity.Image;
import com.example.community.entity.User;
import com.example.community.service.BoardImageService;
import com.example.community.service.BoardService;
import com.example.community.service.ImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class BoardImageCommandFacadeTest {
    @Mock
    private BoardImageService boardImageService;

    @Mock
    private BoardService boardService;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private BoardImageCommandFacade boardImageCommandFacade;

    @Test
    void 입력받은_게시글_이미지_개수만큼_boardImage가_생성된다() {
        //given
        Long boardId = 1L;
        List<Long> imageIds = List.of(10L, 20L, 30L);

        User user = new User("email", "password", "nickname");
        Board board = new Board("title", "content", user);
        ReflectionTestUtils.setField(board, "id", 1L);

        Image img1 = new Image();
        ReflectionTestUtils.setField(img1, "id", 10L);

        Image img2 = new Image();
        ReflectionTestUtils.setField(img2, "id", 20L);

        Image img3 = new Image();
        ReflectionTestUtils.setField(img3, "id", 30L);

        List<Image> images = List.of(img1, img2, img3);

        given(boardService.findById(boardId)).willReturn(board);
        given(imageService.findByIds(imageIds)).willReturn(images);


        //when
        boardImageCommandFacade.mapsImagesToBoard(boardId, imageIds);


        //then
        then(boardImageService).should(times(3)).save(any(Board.class), any(Image.class));;
    }
}