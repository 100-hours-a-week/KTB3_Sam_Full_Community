package com.example.community.service;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.Board;
import com.example.community.entity.User;
import com.example.community.event.BoardSavedEvent;
import com.example.community.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {
    @Mock
    private BoardRepository boardRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private BoardService boardService;

    @Test
    void 게시글이_성공적으로_저장되고_이벤트가_발행된다() {
        // given
        User user = new User("email", "password", "nickname");
        ReflectionTestUtils.setField(user, "id", 1L);

        Board savedBoard = new Board("title", "content", user);
        ReflectionTestUtils.setField(savedBoard, "id", 10L);

        List<Long> imageIds = List.of(1L, 2L);

        given(boardRepository.save(any(Board.class)))
                .willReturn(savedBoard);


        //when
        Board result = boardService.save("title", "content", imageIds, user);


        //then
        assertThat(result.getId()).isEqualTo(10L);
        then(eventPublisher).should(times(1))
                .publishEvent(any(BoardSavedEvent.class));
    }

    @Test
    void 게시글이_성공적으로_수정되고_이벤트가_발행된다() {
        //given
        Long userId = 1L;
        Long boardId = 10L;
        List<Long> boardImageIds = List.of(1L, 2L);

        User user = new User("email", "password", "nickname");
        ReflectionTestUtils.setField(user, "id", userId);

        Board board = new Board("oldTitle", "oldContent", user);
        ReflectionTestUtils.setField(board, "id", boardId);

        given(boardRepository.findById(boardId))
                .willReturn(Optional.of(board));


        //when
        boardService.update(userId, boardId, "newTitle", "newContent", boardImageIds);


        //then
        assertThat(board.getTitle()).isEqualTo("newTitle");
        then(eventPublisher).should(times(1))
                .publishEvent(any(BoardSavedEvent.class));
    }

    @Test
    void 게시글_작성자가_아닌경우_수정이_진행되지않고_미리_지정해둔_예외를_발생시킨다() {
        //given
        Long userId = 1L;
        Long otherUserId = 2L;
        Long boardId = 10L;
        List<Long> boardImageIds = List.of(1L, 2L);

        User user = new User("email", "password", "nickname");
        ReflectionTestUtils.setField(user, "id", 1L);

        Board board = new Board("oldTitle", "oldContent", user);
        ReflectionTestUtils.setField(board, "id", boardId);

        given(boardRepository.findById(boardId))
                .willReturn(Optional.of(board));


        //when
        final BaseException result = assertThrows(BaseException.class,
                () -> boardService.update(otherUserId, boardId, "newTitle", "newContent", boardImageIds));


        //then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.INVALID_REQUEST);
    }

    @Test
    void 입력받은_게시글_작성자_아이디와_게시글_아이디에의해_게시글이_성공적으로_삭제된다() {
        //given
        Long userId = 1L;
        Long boardId = 10L;

        User user = new User("email", "password", "nickname");
        ReflectionTestUtils.setField(user, "id", userId);

        Board board = new Board("title", "content", user);
        ReflectionTestUtils.setField(board, "id", boardId);

        given(boardRepository.findById(boardId))
                .willReturn(Optional.of(board));


        //when
        boardService.delete(userId, boardId);


        //then
        then(boardRepository).should(times(1)).deleteById(boardId);
    }

    @Test
    void 입력받은_게시글_작성자에의해_게시글이_성공적으로_삭제된다() {
        //given
        Long userId = 1L;


        //when
        boardService.deleteByUserId(userId);


        // then
        then(boardRepository).should(times(1)).deleteByUserId(userId);
    }

    @Test
    void 게시글_아이디에의해_게시글이_조회된다() {
        //given
        Board board = new Board("title", "content", new User("email","password","nickname"));
        ReflectionTestUtils.setField(board, "id", 10L);

        given(boardRepository.findById(10L)).willReturn(Optional.of(board));


        //when
        Board result = boardService.findById(10L);


        //then
        assertThat(result.getId()).isEqualTo(10L);
    }

    @Test
    void 게시글_아이디와_일치하는_게시글이_없으면_미리_지정해둔_예외를_반환한다() {
        //given
        given(boardRepository.findById(10L)).willReturn(Optional.empty());


        //when
        final BaseException result = assertThrows(BaseException.class, () -> boardService.findById(10L));


        //then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_BOARD);
    }

    @Test
    void 조건에_맞는_게시글만_페이징되어_반환된다() {
        //given
        User user = new User("email", "password", "nickname");
        ReflectionTestUtils.setField(user, "id", 1L);

        Board board1 = new Board("Hello Title", "This is Content", user);
        Board board2 = new Board("Another Hello", "Content matches", user);
        Board board3 = new Board("Other Title", "Irrelevant text", user);

        List<Board> filteredBoards = List.of(board1, board2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Board> mockPage = new PageImpl<>(filteredBoards, pageable, filteredBoards.size());

        given(boardRepository.findAll(eq("Hello"), eq("Content"), any(Pageable.class)))
                .willReturn(mockPage);


        //when
        Page<Board> result = boardService.findPage("Hello", "Content", 1, 10);


        //then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent())
                .extracting(Board::getTitle)
                .containsExactlyInAnyOrder("Hello Title", "Another Hello");
    }

    @Test
    void 제목이_중복되지않으면_예외를_발생시키지않고_통과시킨다() {
        //given
        given(boardRepository.findByTitle("hello")).willReturn(Optional.empty());


        //when,then
        assertThatCode(() -> boardService.validateTitle("hello")).doesNotThrowAnyException();
    }

    @Test
    void 제목이_중복되면_미리_지정해둔_예외를_반환한다() {
        //given
        User user = new User("email", "password", "nickname");
        Board board = new Board("title", "content", user);

        given(boardRepository.findByTitle("title")).willReturn(Optional.of(board));


        //when
        final BaseException result = assertThrows(BaseException.class, () -> boardService.validateTitle("title"));


        //then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_TITLE);
    }
}