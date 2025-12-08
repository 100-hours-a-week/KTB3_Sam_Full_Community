package com.example.community.service;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.Board;
import com.example.community.entity.Comment;
import com.example.community.entity.User;
import com.example.community.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    void 게시글에_댓글이_성공적으로_저장된다() {
        //given
        User user = new User("email", "password", "nickname");
        Board board = new Board("title", "content", user);
        Comment comment = new Comment(user, board, "hello");

        given(commentRepository.save(any(Comment.class))).willReturn(comment);


        //when
        Comment result = commentService.save(user, board, "hello");


        //then
        assertThat(result.getContent()).isEqualTo("hello");
    }

    @Test
    void 입력받은_게시글_전체에대한_댓글이_모두_조회된다() {
        //given
        List<Long> boardIds = List.of(1L, 2L);

        User user = new User("email", "password", "nickname");
        Board board1 = new Board("title1", "content1", user);
        Board board2 = new Board("title2", "content2", user);

        List<Comment> comments = List.of(
                new Comment(user, board1, "comment1"),
                new Comment(user, board2, "comment2")
        );

        given(commentRepository.findAllByBoardId(boardIds)).willReturn(comments);


        //when
        List<Comment> result = commentService.findAllByPagedBoardIds(boardIds);

        //then
        assertThat(result).hasSize(2);
    }

    @Test
    void 입력받은_단일_게시글에_대한_댓글이_모두_조회된다() {
        //given
        Long boardId = 10L;

        User user = new User("email", "password", "nickname");
        Board board = new Board("title", "content", user);

        List<Comment> comments = List.of(
                new Comment(user, board, "comment1"),
                new Comment(user, board, "comment2")
        );

        given(commentRepository.findAllByBoardId(boardId))
                .willReturn(comments);


        //when
        List<Comment> result = commentService.findAllByBoardId(boardId);


        //then
        assertThat(result).hasSize(2);
    }

    @Test
    void 입력받은_단일_게시글에_대한_댓글이_페이징되어_조회된다() {
        //given
        Long boardId = 10L;

        User user = new User("email", "password", "nickname");
        Board board = new Board("title", "content", user);

        Comment comment1 = new Comment(user, board, "comment1");
        Comment comment2 = new Comment(user, board, "comment2");

        Page<Comment> mockPage = new PageImpl<>(List.of(comment1, comment2));

        given(commentRepository.findAllByBoardId(eq(boardId), any(Pageable.class)))
                .willReturn(mockPage);


        //when
        Page<Comment> result = commentService.findPageByBoardId(boardId, 1, 10);


        //then
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @Test
    void 댓글이_성공적으로_삭제된다() {
        //given
        Long boardId = 100L;


        //when
        commentService.deleteByBoardId(boardId);


        //then
        then(commentRepository).should(times(1)).deleteByBoardId(boardId);
    }

    @Test
    void 등록되어있던_댓글이_성공적으로_수정된다() {
        //given
        Long userId = 1L;
        Long commentId = 10L;

        User user = new User("email", "password", "nickname");
        ReflectionTestUtils.setField(user, "id", userId);

        Board board = new Board("title", "content", user);
        Comment comment = new Comment(user, board, "old");

        given(commentRepository.findById(commentId))
                .willReturn(Optional.of(comment));


        //when
        commentService.updateById(userId, commentId, "newContent");


        //then
        assertThat(comment.getContent()).isEqualTo("newContent");
    }

    @Test
    void 입력받은_유저의_댓글이_성공적으로_삭제된다() {
        // given
        Long userId = 1L;
        Long commentId = 10L;

        User user = new User("email", "password", "nickname");
        ReflectionTestUtils.setField(user, "id", userId);

        Board board = new Board("title", "content", user);
        Comment comment = new Comment(user, board, "content");

        given(commentRepository.findById(commentId))
                .willReturn(Optional.of(comment));


        //when
        commentService.deleteById(userId, commentId);


        // then
        then(commentRepository).should(times(1)).deleteById(commentId);
    }


    @Test
    void 입력받은_유저의_댓글이_아닌경우_삭제가_진행되지않는다() {
        // given
        Long userId = 1L;
        Long otherUserId = 2L;
        Long commentId = 10L;

        User user = new User("email", "password", "nickname");
        ReflectionTestUtils.setField(user, "id", userId);

        Board board = new Board("title", "content", user);
        Comment comment = new Comment(user, board, "content");

        given(commentRepository.findById(commentId))
                .willReturn(Optional.of(comment));


        //when
        final BaseException result = assertThrows(BaseException.class, () ->  commentService.deleteById(otherUserId, commentId));


        // then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.INVALID_REQUEST);
    }
}