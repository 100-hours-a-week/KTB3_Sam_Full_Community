package com.example.community.facade;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.Board;
import com.example.community.entity.Like;
import com.example.community.entity.User;
import com.example.community.service.BoardService;
import com.example.community.service.LikeService;
import com.example.community.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class LikeCommandFacadeTest {
    @Mock
    private UserService userService;

    @Mock
    private BoardService boardService;

    @Mock
    private LikeService likeService;

    @InjectMocks
    private LikeCommandFacade likeCommandFacade;

    @Test
    void 좋아요가_안되어있다면_정상적으로_좋아요가_저장된다() {
        //given
        Long userId = 1L;
        Long boardId = 10L;

        User user = new User("email", "password", "nickname");
        ReflectionTestUtils.setField(user, "id", userId);

        Board board = new Board("title", "content", user);
        ReflectionTestUtils.setField(board, "id", boardId);

        Like savedLike = new Like(user, board);

        given(userService.getUser(userId)).willReturn(user);
        given(boardService.findById(boardId)).willReturn(board);
        given(likeService.findByUserIdAndBoardId(userId, boardId)).willReturn(Optional.empty());
        given(likeService.save(user, board)).willReturn(savedLike);


        //when
        Like result = likeCommandFacade.post(userId, boardId);


        //then
        assertThat(result).isEqualTo(savedLike);
        then(likeService).should().save(user, board);
    }

    @Test
    void 이미_좋아요한_게시글이면_미리_지정해둔_예외가_발생한다() {
        //given
        Long userId = 1L;
        Long boardId = 10L;

        User user = new User("email", "password", "nickname");
        ReflectionTestUtils.setField(user, "id", userId);

        Board board = new Board("title", "content", user);
        ReflectionTestUtils.setField(board, "id", boardId);

        Like existingLike = new Like(user, board);

        given(likeService.findByUserIdAndBoardId(userId, boardId))
                .willReturn(Optional.of(existingLike));


        //when
        final BaseException result = assertThrows(BaseException.class, () -> likeCommandFacade.post(userId, boardId));


        //then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.ALREADY_LIKED_POST);
        then(likeService).should(never()).save(any(), any());
    }
}