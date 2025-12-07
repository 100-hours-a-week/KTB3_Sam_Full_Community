package com.example.community.controller;

import com.example.community.auth.jwt.JwtUtil;
import com.example.community.common.exception.GlobalExceptionHandler;
import com.example.community.entity.Board;
import com.example.community.entity.Like;
import com.example.community.entity.User;
import com.example.community.facade.LikeCommandFacade;
import com.example.community.service.LikeService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LikeControllerTest {
    @Mock
    private LikeService likeService;

    @Mock
    private LikeCommandFacade likeCommandFacade;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private LikeController likeController;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(likeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void 좋아요가_성공적으로_저장되고_좋아요_유저_게시글의_아이디가_반환된다() throws Exception{
        // given
        Long userId = 1L;
        Long boardId = 3L;

        User user = new User("email", "password", "nickname");
        Board board = new Board("title", "content", user);

        Like like = new Like(user, board);
        ReflectionTestUtils.setField(like, "id", 10L);

        given(jwtUtil.extractUserId(anyString())).willReturn(userId);
        given(likeCommandFacade.post(userId, boardId)).willReturn(like);


        //when,then
        mvc.perform(post("/boards/{boardId}/like", boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("accessToken", "accessToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.likeId").value(10L))
                .andExpect(jsonPath("$.data.boardId").value(boardId))
                .andExpect(jsonPath("$.data.userId").value(userId));

    }

    @Test
    void 좋아요가_성공적으로_삭제된다() throws Exception {
        //given
        Long userId = 1L;
        Long boardId = 3L;

        given(jwtUtil.extractUserId(anyString())).willReturn(userId);


        //when,then
        mvc.perform(delete("/boards/{boardId}/like", boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("accessToken", "accessToken"))
                .andExpect(status().isNoContent());
    }

    @Test
    void 입력받은_게시글에_입력받은_유저가_좋아요를_눌렀는지_조회한다() throws Exception {
        //given
        Long userId = 1L;
        Long boardId = 3L;

        given(jwtUtil.extractUserId(anyString())).willReturn(userId);
        given(likeService.checkBoardLiked(userId, boardId)).willReturn(true);


        //when,then
        mvc.perform(get("/boards/{boardId}/like", boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("accessToken", "accessToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isLiked").value(true));
    }
}