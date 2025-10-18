package com.example.community.controller;

import com.example.community.common.SuccessCode;
import com.example.community.dto.AuthToken;
import com.example.community.dto.request.LoginRequest;
import com.example.community.dto.response.ApiResponse;
import com.example.community.dto.response.UserInfoResponse;
import com.example.community.entity.User;
import com.example.community.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;

    AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth")
    public ResponseEntity<ApiResponse<AuthToken>> login (@Valid @RequestBody LoginRequest request) {
        AuthToken userToken = authService.login(request.email(), request.password());
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.USER_LOGIN, userToken));
    }
}
