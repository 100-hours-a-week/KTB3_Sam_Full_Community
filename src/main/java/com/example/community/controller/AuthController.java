package com.example.community.controller;

import com.example.community.auth.JwtUtil;
import com.example.community.common.SuccessCode;
import com.example.community.dto.AuthToken;
import com.example.community.dto.request.LoginRequest;
import com.example.community.dto.request.ReissueRequest;
import com.example.community.dto.response.APIResponse;
import com.example.community.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인증 API", description = "인증 관련 API")
@RestController
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;


    AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "로그인", description = "로그인을 진행한 후 인증된 유저에게 토큰을 발급합니다.")
    @PostMapping("/auth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "login_success"),
            @ApiResponse(responseCode = "400", description = "invalid_request"),
            @ApiResponse(responseCode = "500", description = "internal_server_error")
    })
    public ResponseEntity<APIResponse<AuthToken>> login (@Valid @RequestBody LoginRequest request) {
        AuthToken userToken = authService.login(request.email(), request.password());
        return ResponseEntity.ok(APIResponse.success(SuccessCode.USER_LOGIN, userToken));
    }

    @Operation(summary = "로그아웃", description = "로그아웃을 진행한 후에 인증 정보를 삭제합니다.")
    @DeleteMapping("/auth")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "user_logout_success"),
            @ApiResponse(responseCode = "401", description = "not_login_user"),
            @ApiResponse(responseCode = "500", description = "internal_server_error")
    })
    public ResponseEntity<APIResponse<Void>> logout(HttpServletRequest servletRequest) {
        String token = (String) servletRequest.getAttribute("accessToken");
        Long userId = jwtUtil.extractUserId(token);
        authService.logout(userId, token);
        return ResponseEntity.ok(APIResponse.success(SuccessCode.USER_LOGOUT, null));
    }

    @Operation(summary = "토큰 재발급", description = "토큰을 재발급 해 인증 정보를 갱신합니다.")
    @PutMapping("/auth")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "token_reissue_success"),
            @ApiResponse(responseCode = "400", description = "invalid_request"),
            @ApiResponse(responseCode = "500", description = "internal_server_error")
    })
    public ResponseEntity<APIResponse<AuthToken>> reissue(HttpServletRequest servletRequest, @Valid @RequestBody ReissueRequest request) {
        String accessToken = (String) servletRequest.getAttribute("accessToken");
        Long userId = jwtUtil.extractUserId(accessToken);
        AuthToken userToken = authService.reissue(userId, accessToken,request.refreshToken());
        return ResponseEntity.ok(APIResponse.success(SuccessCode.TOKEN_REISSUED, userToken));
    }
}
