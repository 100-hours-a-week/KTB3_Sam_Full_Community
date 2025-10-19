package com.example.community.controller;

import com.example.community.common.SuccessCode;
import com.example.community.dto.response.ApiResponse;
import com.example.community.dto.response.ImageUrlResponse;
import com.example.community.facade.S3ImageFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {
    private final S3ImageFacade s3ImageFacade;

    ImageController(S3ImageFacade s3ImageFacade) {
        this.s3ImageFacade = s3ImageFacade;
    }

    @PostMapping("/images")
    public ResponseEntity<ApiResponse<ImageUrlResponse>> getUploadImageUrl() {
        ImageUrlResponse imageUrlResponse = s3ImageFacade.getUploadImageUrl();
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.IMAGE_UPLOADED, imageUrlResponse));
    }
}
