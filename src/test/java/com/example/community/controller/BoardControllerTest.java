package com.example.community.controller;

import com.example.community.auth.jwt.JwtUtil;
import com.example.community.common.exception.GlobalExceptionHandler;
import com.example.community.dto.PageInfo;
import com.example.community.dto.PagedData;
import com.example.community.dto.request.BoardPostRequest;
import com.example.community.dto.request.BoardUpdateRequest;
import com.example.community.dto.response.BoardDetailResponse;
import com.example.community.dto.response.BoardInfoResponse;
import com.example.community.entity.Board;
import com.example.community.entity.Image;
import com.example.community.entity.User;
import com.example.community.entity.UserImage;
import com.example.community.facade.BoardCommandFacade;
import com.example.community.facade.BoardQueryFacade;
import com.example.community.service.BoardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class BoardControllerTest {
    @Mock
    private BoardCommandFacade boardCommandFacade;

    @Mock
    private BoardQueryFacade boardQueryFacade;

    @Mock
    private BoardService boardService;

    @Mock
    private JwtUtil jwtUtil;


    @InjectMocks
    private BoardController boardController;

    private MockMvc mvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(boardController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void 게시글생성_요청이_성공적으로_처리된다() throws Exception {
        //given
        Long userId = 10L;
        BoardPostRequest request = new BoardPostRequest("title", "content", List.of(1L, 2L));

        User user = new User("email", "password", "nickname");
        Board board = new Board("title", "content", user);
        ReflectionTestUtils.setField(board, "id", 100L);

        given(jwtUtil.extractUserId(anyString())).willReturn(userId);
        given(boardCommandFacade.post(eq(userId), eq("title"), eq("content"), eq(List.of(1L, 2L))))
                .willReturn(board);


        //when,then
        mvc.perform(post("/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("accessToken", "accessToken")
                        .content(toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("board_create_success"));
    }

    @Test
    void 게시글_목록_전체조회_요청이_성공적으로_처리된다() throws Exception {
        //given
        Long boardId = 1L;

        User user = new User("email", "pw", "nickname");

        Image image = new Image();
        ReflectionTestUtils.setField(image, "id", 777L);

        UserImage userImage = new UserImage(user, image);
        ReflectionTestUtils.setField(user, "userImage", userImage);

        Board board = new Board("title", "content", user);
        ReflectionTestUtils.setField(board, "id", boardId);

        BoardInfoResponse response = BoardInfoResponse.of(
                board, 1, 0, 1, user, image
        );

        PageInfo pageInfo = new PageInfo(0, 10, true, true, 1, 1, false);
        PagedData pagedData = new PagedData(List.of(response), pageInfo);

        given(boardQueryFacade.getAllPagedBoards(eq(null), eq(null), eq(1), eq(10)))
                .willReturn(pagedData);


        //when,then
        mvc.perform(get("/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("accessToken", "accessToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("board_find_all_success"));
    }

    @Test
    void 게시글_상세조회_요청이_성공적으로_처리된다() throws Exception {
        //given
        Long boardId = 1L;

        User user = new User("email", "password", "nickname");
        ReflectionTestUtils.setField(user, "id", 99L);

        Image image = new Image();
        ReflectionTestUtils.setField(image, "id", 777L);

        UserImage ui = new UserImage(user, image);
        ReflectionTestUtils.setField(user, "userImage", ui);

        Board board = new Board("title", "content", user);
        ReflectionTestUtils.setField(board, "id", boardId);

        BoardDetailResponse detail =
                BoardDetailResponse.of(board, 3, 10, 5, List.of(1L,2L), user, 777L);

        given(boardQueryFacade.getBoardDetail(boardId)).willReturn(detail);


        //when,then
        mvc.perform(get("/boards/{id}", boardId)
                        .requestAttr("accessToken", "accessToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.boardId").value(1L))
                .andExpect(jsonPath("$.data.likes").value(3))
                .andExpect(jsonPath("$.data.commentsCount").value(5))
                .andExpect(jsonPath("$.data.nickname").value("nickname"));
    }

    @Test
    void 게시글_수정_요청이_성공적으로_처리된다() throws Exception {
        //given
        Long boardId = 1L;
        Long userId = 10L;

        BoardUpdateRequest request =
                new BoardUpdateRequest("updated", "updated-content", List.of(1L, 2L));

        given(jwtUtil.extractUserId(anyString())).willReturn(userId);


        //when,then
        mvc.perform(put("/boards/{id}", boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("accessToken", "accessToken")
                        .content(toJson(request)))
                .andExpect(status().isNoContent());

        then(boardService).should().update(userId, boardId, "updated", "updated-content", List.of(1L, 2L));
    }

    @Test
    void 게시글_삭제_요청이_성공적으로_처리된다() throws Exception {
        //given
        Long boardId = 1L;
        Long userId = 10L;

        given(jwtUtil.extractUserId(anyString())).willReturn(userId);


        //when,then
        mvc.perform(delete("/boards/{id}", boardId)
                        .requestAttr("accessToken", "accessToken"))
                .andExpect(status().isNoContent());

        then(boardService).should().delete(userId, boardId);
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("JSON 변환 실패", e);
        }
    }
}