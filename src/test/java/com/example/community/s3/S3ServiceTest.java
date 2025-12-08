package com.example.community.s3;

import com.example.community.common.exception.BaseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {
    @Mock
    private S3Presigner preSigner;

    @Mock
    private S3Properties s3Properties;

    @InjectMocks
    private S3Service s3Service;

    @Test
    void 성공적으로_이미지_GET_url을_반환한다() throws Exception {
        // given
        Long imageId = 10L;

        PresignedGetObjectRequest mockGetRequest = mock(PresignedGetObjectRequest.class);
        URL mockUrl = mock(URL.class);

        given(mockUrl.toString()).willReturn("https://mock-url/get");
        given(mockGetRequest.url()).willReturn(mockUrl);

        given(s3Properties.bucket()).willReturn("bucket-name");
        given(preSigner.presignGetObject(any(GetObjectPresignRequest.class)))
                .willReturn(mockGetRequest);


        //when
        String result = s3Service.getCommunityImage(imageId);


        //then
        assertThat(result).isEqualTo("https://mock-url/get");
    }

    @Test
    void 성공적으로_이미지_PUT_url을_반환한다() {
        //given
        Long imageId = 20L;

        PresignedPutObjectRequest mockPutRequest = mock(PresignedPutObjectRequest.class);
        URL mockUrl = mock(URL.class);

        given(mockUrl.toString()).willReturn("https://mock-url/put");
        given(mockPutRequest.url()).willReturn(mockUrl);

        given(s3Properties.bucket()).willReturn("bucket-name");
        given(preSigner.presignPutObject(any(PutObjectPresignRequest.class)))
                .willReturn(mockPutRequest);


        //when
        String result = s3Service.getCommunityImageUploadUrl(imageId);


        //then
        assertThat(result).isEqualTo("https://mock-url/put");
    }
}