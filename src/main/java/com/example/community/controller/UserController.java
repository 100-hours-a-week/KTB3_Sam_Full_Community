package com.example.community.controller;

import com.example.community.common.SuccessCode;
import com.example.community.dto.request.UserModifyRequest;
import com.example.community.dto.request.UserRegisterRequest;
import com.example.community.dto.response.ApiResponse;
import com.example.community.dto.response.UserInfoResponse;
import com.example.community.dto.response.UserRegisterResponse;
import com.example.community.entity.User;
import com.example.community.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<UserRegisterResponse>> registerUser(@Valid @RequestBody UserRegisterRequest request) {
        User user = userService.registerUser(request.email(), request.password(), request.nickname(), request.profileImageId());
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.USER_REGISTERED, UserRegisterResponse.from(user)));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getUser(HttpServletRequest servletRequest) {
        Long userId = (Long) servletRequest.getAttribute("userId");
        User user = userService.getUser(userId);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.USER_FOUND,UserInfoResponse.from(user)));
    }

    @PatchMapping("/users")
    public ResponseEntity<ApiResponse<Void>> modifyUser(HttpServletRequest servletRequest, @Valid @RequestBody UserModifyRequest request) {
        Long userId = (Long) servletRequest.getAttribute("userId");
        userService.modifyUser(userId, request.nickname(), request.profileImageId());
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.USER_INFO_UPDATED,null ));
    }
}
