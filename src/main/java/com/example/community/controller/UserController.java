package com.example.community.controller;

import com.example.community.auth.JwtUtil;
import com.example.community.common.SuccessCode;
import com.example.community.dto.request.PasswordModifyRequest;
import com.example.community.dto.request.UserModifyRequest;
import com.example.community.dto.request.UserRegisterRequest;
import com.example.community.dto.response.APIResponse;
import com.example.community.dto.response.UserInfoResponse;
import com.example.community.dto.response.UserRegisterResponse;
import com.example.community.entity.User;
import com.example.community.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "회원가입", description = "사용자의 정보를 받아 회원가입을 진행합니다.")
    @PostMapping("/users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "register_success"),
            @ApiResponse(responseCode = "400", description = "invalid_request"),
            @ApiResponse(responseCode = "500", description = "internal_server_error")
    })
    public ResponseEntity<APIResponse<UserRegisterResponse>> registerUser(@Valid @RequestBody UserRegisterRequest request) {
        User user = userService.registerUser(request.email(), request.password(), request.nickname(), request.profileImageId());
        return ResponseEntity.ok(APIResponse.success(SuccessCode.USER_REGISTERED, UserRegisterResponse.from(user)));
    }

    @GetMapping("/users")
    public ResponseEntity<APIResponse<UserInfoResponse>> getUser(HttpServletRequest servletRequest) {
        Long userId = jwtUtil.extractUserId((String) servletRequest.getAttribute("accessToken"));
        User user = userService.getUser(userId);
        return ResponseEntity.ok(APIResponse.success(SuccessCode.USER_FOUND,UserInfoResponse.from(user)));
    }

    @PatchMapping("/users")
    public ResponseEntity<APIResponse<Void>> modifyUser(HttpServletRequest servletRequest, @Valid @RequestBody UserModifyRequest request) {
        Long userId = jwtUtil.extractUserId((String) servletRequest.getAttribute("accessToken"));
        userService.modifyUser(userId, request.nickname(), request.profileImageId());
        return ResponseEntity.ok(APIResponse.success(SuccessCode.USER_INFO_UPDATED,null ));
    }

    @PatchMapping("/users/password")
    public ResponseEntity<APIResponse<Void>> updatePassword(HttpServletRequest servletRequest, @Valid @RequestBody PasswordModifyRequest request) {
        Long userId = jwtUtil.extractUserId((String) servletRequest.getAttribute("accessToken"));
        userService.changePassword(userId, request.password(), request.checkPassword());
        return ResponseEntity.ok(APIResponse.success(SuccessCode.PASSWORD_UPDATED, null));
    }

    @DeleteMapping("/users")
    public ResponseEntity<APIResponse<Void>> deleteUser(HttpServletRequest servletRequest) {
        Long userId = jwtUtil.extractUserId((String) servletRequest.getAttribute("accessToken"));
        userService.deleteUser(userId);
        return ResponseEntity.ok(APIResponse.success(SuccessCode.USER_DELETED, null));
    }
}
