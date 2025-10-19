package com.example.community.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {
    @Bean
    public S3Properties s3Properties(
            @Value("${spring.cloud.aws.s3.access-key}") String accessKey,
            @Value("${spring.cloud.aws.s3.secret-key}") String secretKey,
            @Value("${spring.cloud.aws.s3.bucket}") String bucket
    ) {
        return new S3Properties(accessKey, secretKey, bucket);
    }

    @Bean
    public S3Presigner preSigner(S3Properties s3Properties) {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(s3Properties.accessKey(), s3Properties.secretKey());

        return S3Presigner.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.AP_NORTHEAST_2)
                .build();
    }
}
