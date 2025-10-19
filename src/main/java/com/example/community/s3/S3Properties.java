package com.example.community.s3;

import org.springframework.boot.context.properties.ConfigurationProperties;

public record S3Properties(
        String accessKey,
        String secretKey,
        String bucket
) {

}
