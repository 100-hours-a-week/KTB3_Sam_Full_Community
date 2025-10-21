package com.example.community.controller;

import com.example.community.common.SuccessCode;
import com.example.community.dto.response.APIResponse;
import com.example.community.dto.response.ImageUrlResponse;
import com.example.community.facade.S3ImageFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "이미지 API", description = "이미지 업로드,조회 관련 API")
@RestController
public class ImageController {
    private final S3ImageFacade s3ImageFacade;

    ImageController(S3ImageFacade s3ImageFacade) {
        this.s3ImageFacade = s3ImageFacade;
    }

    @Operation(summary = "이미지 업로드", description = "이미지를 업로드 할 수 있는 url을 반환합니다.")
    @PostMapping("/images")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "image_upload_success"),
            @ApiResponse(responseCode = "400", description = "invalid_request"),
            @ApiResponse(responseCode = "500", description = "internal_server_error")
    })
    public ResponseEntity<APIResponse<ImageUrlResponse>> getUploadImageUrl() {
        ImageUrlResponse imageUrlResponse = s3ImageFacade.getUploadImageUrl();
        return ResponseEntity.ok(APIResponse.success(SuccessCode.IMAGE_UPLOADED, imageUrlResponse));
    }

    @Operation(summary = "이미지 조회", description = "이미지 ID에 해당하는 이미지를 조회할 수 있는 url을 반환합니다.")
    @GetMapping("/images/{id}")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "image_find_success"),
            @ApiResponse(responseCode = "404", description = "not_found_image"),
            @ApiResponse(responseCode = "500", description = "internal_server_error")
    })
    public ResponseEntity<APIResponse<ImageUrlResponse>> getImageUrl(
            @Parameter(description = "이미지 ID", required = true, example = "2")
            @PathVariable("id") Long id) {
        ImageUrlResponse imageUrlResponse = s3ImageFacade.getImageUrl(id);
        return ResponseEntity.ok(APIResponse.success(SuccessCode.IMAGE_FOUND, imageUrlResponse));
    }
}
