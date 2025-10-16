package com.example.community.controller;

import com.example.community.dto.request.UserRegisterRequest;
import com.example.community.dto.response.UserRegisterResponse;
import com.example.community.entity.User;
import com.example.community.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")

    public UserRegisterResponse registerUser(UserRegisterRequest request) {
        User user = userService.registerUser(request.email(), request.password(), request.nickname(), request.profileImageId());
        return UserRegisterResponse.from(user);
    }
}
