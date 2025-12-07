package com.example.community.facade;

import com.example.community.dto.PagedData;
import com.example.community.dto.response.BoardDetailResponse;
import com.example.community.dto.response.BoardInfoResponse;
import com.example.community.entity.*;
import com.example.community.service.BoardImageService;
import com.example.community.service.BoardService;
import com.example.community.service.CommentService;
import com.example.community.service.LikeService;
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
class BoardQueryFacadeTest {
    @Mock
    BoardService boardService;

    @Mock
    BoardImageService boardImageService;

    @Mock
    CommentService commentService;

    @Mock
    LikeService likeService;

    @InjectMocks
    BoardQueryFacade boardQueryFacade;

    @Test
    void 페이징된_게시글_전체가_반환된다() {
        // given
        Long boardId = 1L;

        User user = new User("user1@test.com", "password", "nickname1");
        ReflectionTestUtils.setField(user, "id", 100L);

        Image image = new Image();
        ReflectionTestUtils.setField(image, "id", 999L);

        UserImage userImage = new UserImage(user, image);
        ReflectionTestUtils.setField(user, "userImage", userImage);

        Board board = new Board("title", "content", user);
        ReflectionTestUtils.setField(board, "id", boardId);

        Page<Board> mockBoardPage = new PageImpl<>(List.of(board), PageRequest.of(0, 10), 1);

        User likeUser1 = new User("user2@test.com", "password", "nickname2");
        User likeUser2 = new User("user3@test.com", "password", "nickname3");
        ReflectionTestUtils.setField(likeUser1, "id", 200L);
        ReflectionTestUtils.setField(likeUser2, "id", 300L);

        Like like1 = new Like(likeUser1, board);
        Like like2 = new Like(likeUser2, board);

        Comment comment = new Comment(user, board, "content");


        given(boardService.findPage(null, null, 1, 10)).willReturn(mockBoardPage);

        given(likeService.findAllByPagedBoardIds(List.of(boardId)))
                .willReturn(List.of(like1, like2));

        given(commentService.findAllByPagedBoardIds(List.of(boardId)))
                .willReturn(List.of(comment));


        //when
        PagedData result = boardQueryFacade.getAllPagedBoards(null, null, 1, 10);


        //then
        BoardInfoResponse info = (BoardInfoResponse) result.content().getFirst();

        assertThat(info.likes()).isEqualTo(2);
        assertThat(info.commentsCount()).isEqualTo(1);
        assertThat(info.title()).isEqualTo("title");
    }

    @Test
    void 게시글_상세정보를_반환한다() {
        //given
        Long boardId = 77L;

        User user = new User("email", "password", "nickname");
        ReflectionTestUtils.setField(user, "id", 1L);

        Image profileImage = new Image();
        ReflectionTestUtils.setField(profileImage, "id", 55L);

        UserImage userImage = new UserImage(user, profileImage);
        ReflectionTestUtils.setField(user, "userImage", userImage);

        Board board = new Board("title", "content", user);
        ReflectionTestUtils.setField(board, "id", boardId);

        Comment comment1 = new Comment(user, board, "content1");
        Comment comment2 = new Comment(user, board, "content2");

        Like like1 = new Like(user, board);

        Image img1 = new Image();
        Image img2 = new Image();
        ReflectionTestUtils.setField(img1, "id", 100L);
        ReflectionTestUtils.setField(img2, "id", 200L);

        BoardImage boardImage1 = new BoardImage(board, img1);
        BoardImage boardImage2 = new BoardImage(board, img2);

        given(boardService.findById(boardId)).willReturn(board);
        given(commentService.findAllByBoardId(boardId)).willReturn(List.of(comment1, comment2));
        given(likeService.findAllByBoard(boardId)).willReturn(List.of(like1));
        given(boardImageService.findByBoardId(boardId)).willReturn(List.of(boardImage1, boardImage2));



        //when
        BoardDetailResponse result = boardQueryFacade.getBoardDetail(boardId);



        // then
        assertThat(result.boardId()).isEqualTo(boardId);
        assertThat(result.title()).isEqualTo("title");
        assertThat(result.content()).isEqualTo("content");
        assertThat(result.likes()).isEqualTo(1);
        assertThat(result.commentsCount()).isEqualTo(2);
        assertThat(result.boardImageIds()).containsExactly(100L, 200L);
        assertThat(result.profileImageId()).isEqualTo(55L);
    }
}