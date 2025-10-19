package com.example.community.s3;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.cloud.aws.s3")
public record S3Properties(
        String accessKey,
        String secretKey,
        String bucket
) {

}
