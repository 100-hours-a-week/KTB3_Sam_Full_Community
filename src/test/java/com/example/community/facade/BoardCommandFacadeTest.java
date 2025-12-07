package com.example.community.facade;

import com.example.community.entity.Board;
import com.example.community.entity.User;
import com.example.community.service.BoardService;
import com.example.community.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class BoardCommandFacadeTest {
    @Mock
    BoardService boardService;

    @Mock
    UserService userService;

    @InjectMocks
    BoardCommandFacade boardCommandFacade;

    @Test
    void 게시글작성자_정보와함께_게시글_저장이_성공적으로_이루어진다() {
        //given
        Long userId = 1L;
        String title = "not duplicated title";
        String content = "content";
        List<Long> boardImageIds = List.of(1L, 2L, 3L);

        User user = new User("email", "password", "nickname");
        ReflectionTestUtils.setField(user, "id", userId);

        Board expectedBoard = new Board(title, content, user);

        willDoNothing().given(boardService).validateTitle(title);
        given(userService.getUser(userId)).willReturn(user);
        given(boardService.save(title, content, boardImageIds, user))
                .willReturn(expectedBoard);


        //when
        Board result = boardCommandFacade.post(userId, title, content, boardImageIds);


        //then
        assertThat(result).isEqualTo(expectedBoard);
        then(boardService).should(times(1))
                .save(title, content, boardImageIds, user);
    }
}