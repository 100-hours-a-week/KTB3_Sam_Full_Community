package com.example.community.s3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {
    private final S3Properties s3Properties;

    S3Config(S3Properties s3Properties) {
        this.s3Properties = s3Properties;
    }

    @Bean
    public S3Presigner preSigner() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(s3Properties.accessKey(), s3Properties.secretKey());

        return S3Presigner.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.AP_NORTHEAST_2)
                .build();
    }
}
