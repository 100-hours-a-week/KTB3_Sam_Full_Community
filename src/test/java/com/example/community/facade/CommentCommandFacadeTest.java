package com.example.community.facade;

import com.example.community.entity.Board;
import com.example.community.entity.Comment;
import com.example.community.entity.User;
import com.example.community.service.BoardService;
import com.example.community.service.CommentService;
import com.example.community.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommentCommandFacadeTest {
    @Mock
    private CommentService commentService;

    @Mock
    private UserService userService;

    @Mock
    private BoardService boardService;

    @InjectMocks
    private CommentCommandFacade commentCommandFacade;

    @Test
    void 입력받은_게시글과_유저에대해_댓글을_생성한다() {
        //given
        Long userId = 1L;
        Long boardId = 10L;
        String content = "hello comment";

        User user = new User("email", "password", "nickname");
        ReflectionTestUtils.setField(user, "id", userId);

        Board board = new Board("title", "content", user);
        ReflectionTestUtils.setField(board, "id", boardId);

        Comment expectedComment = new Comment(user, board, content);

        given(userService.getUser(userId)).willReturn(user);
        given(boardService.findById(boardId)).willReturn(board);
        given(commentService.save(user, board, content)).willReturn(expectedComment);


        //when
        Comment result = commentCommandFacade.post(userId, boardId, content);


        //then
        assertThat(result.getContent()).isEqualTo(content);
        assertThat(result.getUser().getId()).isEqualTo(userId);
        assertThat(result.getBoard().getId()).isEqualTo(boardId);
    }
}