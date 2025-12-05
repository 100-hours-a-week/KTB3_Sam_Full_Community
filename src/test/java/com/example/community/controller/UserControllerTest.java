package com.example.community.controller;

import com.example.community.auth.jwt.JwtUtil;
import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.common.exception.GlobalExceptionHandler;
import com.example.community.dto.request.UserModifyRequest;
import com.example.community.dto.request.UserRegisterRequest;
import com.example.community.dto.response.UserInfoResponse;
import com.example.community.entity.User;
import com.example.community.facade.UserImageQueryFacade;
import com.example.community.repository.UserRepository;
import com.example.community.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private UserImageQueryFacade userImageQueryFacade;

    @Mock
    private JwtUtil jwtUtil;

    private MockMvc mvc;


    private JacksonTester<UserRegisterRequest> userRegisterRequest;
    private JacksonTester<UserModifyRequest> userModifyRequest;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        JacksonTester.initFields(this, objectMapper);
    }


    @Test
    void 회원가입이_성공적으로_이루어져_유저아이디를_반환한다() throws Exception{
        //given
        UserRegisterRequest validRequest = new UserRegisterRequest(
          "email", "password", "nickname", 3L
        );
        User mockUser = new User("email", "password","nickname");
        ReflectionTestUtils.setField(mockUser, "id", 1L);

        given(userService.registerUser(any(), any(), any(), any())).willReturn(mockUser);


        //when, then
        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userRegisterRequest.write(validRequest).getJson()))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(1L));
    }

    @Test
    void 이메일이_중복된경우_회원가입이_이루어지지않고_에러내용을_전달한다() throws Exception{
        //given
        UserRegisterRequest conflictRequest = new UserRegisterRequest(
          "email", "password", "nickname", 1L
        );

        given(userService.registerUser(any(), any(), any(), any())).willThrow(new BaseException(ErrorCode.ALREADY_REGISTERED_EMAIL));


        //when, then
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRegisterRequest.write(conflictRequest).getJson()))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.ALREADY_REGISTERED_EMAIL.getMessage()));
    }

    @Test
    void 비밀번호가_중복된경우_회원가입이_이루어지지않고_에러내용을_전달한다() throws Exception {
        //given
        UserRegisterRequest conflictRequest = new UserRegisterRequest(
                "email", "password", "nickname", 1L
        );

        given(userService.registerUser(any(), any(), any(), any())).willThrow(new BaseException(ErrorCode.ALREADY_REGISTERED_NICKNAME));


        //when, then
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRegisterRequest.write(conflictRequest).getJson()))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.ALREADY_REGISTERED_NICKNAME.getMessage()));
    }

    @Test
    void 토큰과_일치하는_유저가_존재하는경우_성공적으로_유저정보를_반환한다() throws Exception{
        //given
        Long userId = 6L;
        String nickname = "nickname";
        String email = "email";
        Long profileImageId = 3L;

        String accessToken = "accessToken";

        UserInfoResponse response = new UserInfoResponse(userId, nickname, email, profileImageId);
        given(userImageQueryFacade.getUser(userId)).willReturn(response);

        given(jwtUtil.extractUserId(accessToken)).willReturn(userId);


        //when, then
        mvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("accessToken", accessToken))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("user_find_success"));
    }

    @Test
    void 토큰과_일치하는_유저가_존재하는경우_성공적으로_유저정보를_수정한다() throws Exception {
        //given
        String accessToken = "accessToken";
        Long userId = 6L;
        UserModifyRequest modifyRequest = new UserModifyRequest("changeNickname", 100L);

        given(jwtUtil.extractUserId(accessToken)).willReturn(userId);


        //when,then
        mvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userModifyRequest.write(modifyRequest).getJson())
                .requestAttr("accessToken", accessToken))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("user_info_update_success"));

    }

    @Test
    void updatePassword() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void checkEmailDuplicated() {
    }

    @Test
    void checkNicknameDuplicated() {
    }

}