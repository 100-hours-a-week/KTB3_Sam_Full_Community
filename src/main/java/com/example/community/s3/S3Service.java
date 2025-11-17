package com.example.community.s3;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Service
public class S3Service {
    private static final String COMMUNITY_IMAGE_FOLDER = "community";

    private final S3Properties s3Properties;
    private final S3Presigner preSigner;

    S3Service(S3Properties s3Properties, S3Presigner preSigner) {
        this.s3Properties = s3Properties;
        this.preSigner = preSigner;
    }

    public String getCommunityImage(Long imageId) {
        return getPreSignedUrl(COMMUNITY_IMAGE_FOLDER, imageId.toString());
    }

    public String getCommunityImageUploadUrl(Long imageId) {
        return getPreSignedPutUrl(COMMUNITY_IMAGE_FOLDER, imageId.toString());
    }

    private String getPreSignedUrl(String folder, String filename) {
        if (filename == null || filename.isEmpty()) {
            return null;
        }
        return preSigner
                .presignGetObject(getObjectPresignRequest(folder, filename))
                .url()
                .toString();
    }

    private GetObjectPresignRequest getObjectPresignRequest(String folder, String filename) {
        return GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(1))
                .getObjectRequest(objectRequest ->
                        objectRequest
                                .bucket(s3Properties.bucket())
                                .key(String.join("/", folder, filename)))
                .build();
    }

    private String getPreSignedPutUrl(String folder, String filename) {
        if (filename == null || filename.isEmpty()) {
            return null;
        }

        return preSigner
                .presignPutObject(putObjectPresignRequest(folder, filename))
                .url()
                .toString();
    }

    private PutObjectPresignRequest putObjectPresignRequest(String folder, String filename) {
        return PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(1))
                .putObjectRequest(objectRequest ->
                        objectRequest
                                .bucket(s3Properties.bucket())
                                .key(String.join("/", folder, filename))
                )
                .build();
    }
}
