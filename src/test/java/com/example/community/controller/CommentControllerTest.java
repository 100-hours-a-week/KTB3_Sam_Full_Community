package com.example.community.controller;

import com.example.community.auth.jwt.JwtUtil;
import com.example.community.common.exception.GlobalExceptionHandler;
import com.example.community.dto.PageInfo;
import com.example.community.dto.PagedData;
import com.example.community.dto.request.CommentModifyRequest;
import com.example.community.dto.request.CommentPostRequest;
import com.example.community.dto.response.CommentInfoResponse;
import com.example.community.entity.Board;
import com.example.community.entity.Comment;
import com.example.community.entity.User;
import com.example.community.facade.CommentCommandFacade;
import com.example.community.facade.CommentQueryFacade;
import com.example.community.service.CommentService;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {
    @Mock
    private CommentCommandFacade commentCommandFacade;

    @Mock
    private CommentQueryFacade commentQueryFacade;

    @Mock
    private CommentService commentService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private CommentController commentController;

    private MockMvc mvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(commentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void 댓글이_성공적으로_생성된다() throws Exception {
        //given
        Long boardId = 1L;
        Long userId = 10L;

        User user = new User("email", "password", "nickname");
        Board board = new Board("title", "content", user);

        Comment comment = new Comment(user,board,"content");
        ReflectionTestUtils.setField(comment, "id", 100L);

        given(jwtUtil.extractUserId(anyString())).willReturn(userId);
        given(commentCommandFacade.post(eq(userId), eq(boardId), eq("content")))
                .willReturn(comment);

        CommentPostRequest request = new CommentPostRequest("content");


        //when,then
        mvc.perform(post("/boards/{boardId}/comments", boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("accessToken", "accessToken")
                        .content(toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.commentId").value(100L));
    }

    @Test
    void 댓글정보가_페이징되어_성공적으로_조회된다() throws Exception {
        //given
        Long boardId = 1L;

        CommentInfoResponse commentInfo = new CommentInfoResponse(
                100L,
                LocalDateTime.now(),
                "content",
                boardId,
                "nickname",
                999L
        );

        PageInfo pageInfo = new PageInfo(1, 10, true, true, 1, 1, false);
        PagedData<CommentInfoResponse> pagedData =
                new PagedData<>(List.of(commentInfo), pageInfo);

        given(commentQueryFacade.getAllPagedCommentsByBoardId(boardId, 1, 10))
                .willReturn(pagedData);


        //when,then
        mvc.perform(get("/boards/{boardId}/comments", boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("accessToken", "accessToken")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].commentId").value(100L))
                .andExpect(jsonPath("$.data[0].boardId").value(boardId));
    }

    @Test
    void 댓글이_성공적으로_수정된다() throws Exception{
        //given
        Long commentId = 100L;
        Long userId = 10L;

        given(jwtUtil.extractUserId(anyString())).willReturn(userId);

        CommentModifyRequest request = new CommentModifyRequest("modified content");


        //when,then
        mvc.perform(put("/comments/{id}", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("accessToken", "accessToken")
                        .content(toJson(request)))
                .andExpect(status().isNoContent());

        then(commentService).should()
                .updateById(eq(userId), eq(commentId), eq("modified content"));
    }

    @Test
    void 댓글이_성공적으로_삭제된다() throws Exception{
        //given
        Long commentId = 100L;
        Long userId = 10L;

        given(jwtUtil.extractUserId(anyString())).willReturn(userId);


        //when,then
        mvc.perform(delete("/comments/{id}", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("accessToken", "accessToken"))
                .andExpect(status().isNoContent());

        then(commentService).should().deleteById(eq(userId), eq(commentId));
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("JSON 변환 실패", e);
        }
    }
}