package com.example.community.facade;

import com.example.community.dto.PageInfo;
import com.example.community.dto.PagedData;
import com.example.community.dto.response.CommentInfoResponse;
import com.example.community.entity.*;
import com.example.community.service.CommentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CommentQueryFacadeTest {
    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentQueryFacade commentQueryFacade;

    @Test
    void 입력받은_게시글_아이디에_해당하는_댓글이_페이징되어_반환된다() {
        //given
        Long boardId = 1L;
        int page = 1;
        int size = 10;

        User user = new User("email", "password", "nickname");
        ReflectionTestUtils.setField(user, "id", 100L);

        Image image = new Image();
        ReflectionTestUtils.setField(image, "id", 999L);

        UserImage userImage = new UserImage(user, image);
        ReflectionTestUtils.setField(user, "userImage", userImage);

        Board board = new Board("title", "content", user);
        Board board2 = new Board("title2", "content2", user);

        Comment comment1 = new Comment(user, board, "comment1");
        Comment comment2 = new Comment(user, board, "comment2");
        Comment comment3 = new Comment(user,board2, "comment3");

        Page<Comment> mockPage = new PageImpl<>(
                List.of(comment1, comment2),
                PageRequest.of(page - 1, size),
                2
        );

        given(commentService.findPageByBoardId(boardId, page, size))
                .willReturn(mockPage);


        //when
        PagedData result = commentQueryFacade.getAllPagedCommentsByBoardId(boardId, page, size);


        // then
        assertThat(result.content()).hasSize(2);

        CommentInfoResponse first = (CommentInfoResponse) result.content().getFirst();
        assertThat(first.nickname()).isEqualTo("nickname");
        assertThat(first.profileImageId()).isEqualTo(999L);

        then(commentService).should(times(1))
                .findPageByBoardId(boardId, page, size);
    }
}