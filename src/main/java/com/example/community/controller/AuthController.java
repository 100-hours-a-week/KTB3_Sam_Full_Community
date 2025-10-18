package com.example.community.controller;

import com.example.community.auth.JwtUtil;
import com.example.community.common.SuccessCode;
import com.example.community.dto.AuthToken;
import com.example.community.dto.request.LoginRequest;
import com.example.community.dto.response.ApiResponse;
import com.example.community.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;


    AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/auth")
    public ResponseEntity<ApiResponse<AuthToken>> login (@Valid @RequestBody LoginRequest request) {
        AuthToken userToken = authService.login(request.email(), request.password());
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.USER_LOGIN, userToken));
    }

    @DeleteMapping("/auth")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest servletRequest) {
        String token = (String) servletRequest.getAttribute("accessToken");
        Long userId = jwtUtil.extractUserId(token);
        authService.logout(userId, token);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.USER_LOGOUT, null));
    }
}
