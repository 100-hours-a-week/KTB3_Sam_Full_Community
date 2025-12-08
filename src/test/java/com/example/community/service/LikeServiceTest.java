package com.example.community.service;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.Board;
import com.example.community.entity.Like;
import com.example.community.entity.User;
import com.example.community.repository.LikeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {
    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private LikeService likeService;

    @Test
    void 기존에_존재하던_게시글에_좋아요가_성공적으로_저장된다() {
        //given
        User user = new User("email", "password", "nickname");
        Board board = new Board("title", "content", user);

        Like savedLike = new Like(user, board);

        given(likeRepository.save(any(Like.class))).willReturn(savedLike);


        //when
        Like result = likeService.save(user, board);


        //then
        assertThat(result.getUser()).isEqualTo(user);
        assertThat(result.getBoard()).isEqualTo(board);
    }

    @Test
    void 입력받은_유저와_게시글에대한_좋아요를_반환한다() {
        //given
        User user = new User("email", "password", "nickname");
        Board board = new Board("title", "content", user);
        Like like = new Like(user, board);

        given(likeRepository.findByUserIdAndBoardId(user.getId(), board.getId()))
                .willReturn(Optional.of(like));


        //when
        Optional<Like> result = likeService.findByUserIdAndBoardId(user.getId(), board.getId());


        //then
        assertThat(result).isPresent();
        assertThat(result.get().getUser()).isEqualTo(user);
        assertThat(result.get().getBoard()).isEqualTo(board);
    }

    @Test
    void 게시글과_일치하는_좋아요를_모두_조회한다() {
        //given
        User user1 = new User("user1@test.com", "password", "nickname1");
        User user2 = new User("user2@test.com", "password", "nickname2");

        Board board = new Board("title", "content", user1);

        Like like1 = new Like(user1, board);
        Like like2 = new Like(user2, board);

        List<Like> likes = List.of(like1, like2);

        given(likeRepository.findAllByBoardId(board.getId()))
                .willReturn(likes);


        //when
        List<Like> result = likeService.findAllByBoard(board.getId());


        //then
        assertThat(result).hasSize(2);
    }

    @Test
    void 게시글_여러개에_대한_좋아요를_조회한다() {
        //given
        User user = new User("email", "password", "nickname");

        Board board1 = new Board("title1", "content1", user);
        Board board2 = new Board("title2", "content2", user);

        Like like1 = new Like(user, board1);
        Like like2 = new Like(user, board2);

        List<Long> boardIds = List.of(1L, 2L);
        List<Like> likes = List.of(like1, like2);

        given(likeRepository.findAllByBoardIds(boardIds))
                .willReturn(likes);


        //when
        List<Like> result = likeService.findAllByPagedBoardIds(boardIds);


        //then
        assertThat(result).hasSize(2);
    }


    @Test
    void 유저와_게시글에_해당하는_좋아요가_성공적으로_삭제된다() {
        // given
        User user = new User("email", "password", "nickname");
        Board board = new Board("title","content", user);
        Like like = new Like(user, board);
        ReflectionTestUtils.setField(like, "id", 100L);

        given(likeRepository.findByUserIdAndBoardId(user.getId(), board.getId()))
                .willReturn(Optional.of(like));


        //when
        likeService.deleteByUserIdAndBoardId(user.getId(), board.getId());


        //then
        then(likeRepository).should(times(1)).deleteById(100L);
    }

    @Test
    void 좋아요를_찾을수없는경우_삭제를_진행하지않고_미리_지정해둔_예외를_반환한다() {
        // given
        Long userId = 5L;
        Long boardId = 6L;

        given(likeRepository.findByUserIdAndBoardId(userId, boardId))
                .willReturn(Optional.empty());


        //when
        final BaseException result = assertThrows(BaseException.class, () -> likeService.deleteByUserIdAndBoardId(userId, boardId));


        //then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_LIKE);
    }

    @Test
    void 게시글_좋아요_여부를_조회한다() {
        // given
        Long userId = 1L;
        Long boardId = 5L;

        given(likeRepository.findByUserIdAndBoardId(userId, boardId))
                .willReturn(Optional.empty());


        //when
        boolean liked = likeService.checkBoardLiked(userId, boardId);


        //then
        assertThat(liked).isFalse();
    }
}