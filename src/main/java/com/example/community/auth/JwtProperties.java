package com.example.community.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String secret,
        Long accessExpirationMs,
        Long refreshExpirationMs
) {
}
