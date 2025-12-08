package com.example.community.controller;

import com.example.community.auth.jwt.JwtUtil;
import com.example.community.common.exception.GlobalExceptionHandler;
import com.example.community.dto.AuthToken;
import com.example.community.dto.request.LoginRequest;
import com.example.community.dto.request.ReissueRequest;
import com.example.community.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock
    private AuthService authService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    private MockMvc mvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void 로그인_성공시_토큰을_반환한다() throws Exception {
        //given
        LoginRequest request = new LoginRequest("email", "password");
        AuthToken token = new AuthToken("accessToken", "refreshToken");

        given(authService.login(anyString(), anyString())).willReturn(token);


        //when,then
        mvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.data.refreshToken").value("refreshToken"));
    }

    @Test
    void 로그아웃_성공시_204를_반환한다() throws Exception {
        //given
        String accessToken = "accessToken";
        Long userId = 1L;

        given(jwtUtil.extractUserId(accessToken)).willReturn(userId);

        //when,then
        mvc.perform(delete("/auth")
                        .requestAttr("accessToken", accessToken))
                .andExpect(status().isNoContent());
        then(authService).should(times(1)).logout(userId, accessToken);
    }

    @Test
    void 토큰_재발급_성공시_새로운_토큰을_반환한다() throws Exception {
        //given
        Long userId = 1L;
        String oldAccessToken = "accessToken";
        ReissueRequest request = new ReissueRequest("refreshToken");

        AuthToken newToken = new AuthToken("newAccessToken", "newRefreshToken");

        given(jwtUtil.extractUserId(oldAccessToken)).willReturn(userId);
        given(authService.reissue(userId, oldAccessToken, "refreshToken"))
                .willReturn(newToken);


        //when,then
        mvc.perform(put("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request))
                        .requestAttr("accessToken", oldAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value("newAccessToken"))
                .andExpect(jsonPath("$.data.refreshToken").value("newRefreshToken"));
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("JSON 변환 실패", e);
        }
    }
}