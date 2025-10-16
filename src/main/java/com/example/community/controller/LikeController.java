package com.example.community.controller;

import com.example.community.service.LikeService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LikeController {
    private final LikeService likeService;

    LikeController(LikeService likeService) {
        this.likeService = likeService;
    }
}
