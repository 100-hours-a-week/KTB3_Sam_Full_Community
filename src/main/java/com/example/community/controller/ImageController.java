package com.example.community.controller;

import com.example.community.common.SuccessCode;
import com.example.community.dto.response.APIResponse;
import com.example.community.dto.response.ImageUrlResponse;
import com.example.community.facade.S3ImageFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {
    private final S3ImageFacade s3ImageFacade;

    ImageController(S3ImageFacade s3ImageFacade) {
        this.s3ImageFacade = s3ImageFacade;
    }

    @PostMapping("/images")
    public ResponseEntity<APIResponse<ImageUrlResponse>> getUploadImageUrl() {
        ImageUrlResponse imageUrlResponse = s3ImageFacade.getUploadImageUrl();
        return ResponseEntity.ok(APIResponse.success(SuccessCode.IMAGE_UPLOADED, imageUrlResponse));
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<APIResponse<ImageUrlResponse>> getImageUrl(@PathVariable("id") Long id) {
        ImageUrlResponse imageUrlResponse = s3ImageFacade.getImageUrl(id);
        return ResponseEntity.ok(APIResponse.success(SuccessCode.IMAGE_FOUND, imageUrlResponse));
    }
}
