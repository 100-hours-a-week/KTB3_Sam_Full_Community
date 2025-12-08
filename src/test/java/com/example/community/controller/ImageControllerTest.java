package com.example.community.controller;

import com.example.community.common.exception.GlobalExceptionHandler;
import com.example.community.dto.response.ImageUrlResponse;
import com.example.community.facade.S3ImageFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ImageControllerTest {
    @Mock
    private S3ImageFacade s3ImageFacade;

    @InjectMocks
    private ImageController imageController;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(imageController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void 이미지_업로드_url을_성공적으로_반환한다() throws Exception {
        //given
        ImageUrlResponse response = new ImageUrlResponse(1L, "upload-url");
        given(s3ImageFacade.getImageUploadUrl()).willReturn(response);


        //when,then
        mvc.perform(post("/images")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("image_upload_success"));
    }

    @Test
    void 이미지_URL을_성공적으로_조회한다() throws Exception {
        //given
        Long imageId = 3L;
        ImageUrlResponse response = new ImageUrlResponse(imageId, "image-url");
        given(s3ImageFacade.getImageUrl(imageId)).willReturn(response);


        //when,then
        mvc.perform(get("/images/{id}", imageId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("image_find_success"));
    }
}