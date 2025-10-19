package com.example.community.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
    private final Key key;
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;

    public JwtUtil(
            JwtProperties jwtProperties
    ) {
        this.key = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes());
        this.accessTokenExpirationMs = jwtProperties.accessExpirationMs();
        this.refreshTokenExpirationMs = jwtProperties.refreshExpirationMs();
    }

    public String generateAccessToken(Long userId) {
        return generateToken(Map.of("userId", userId), accessTokenExpirationMs);
    }


    public String generateRefreshToken(Long userId) {
        return generateToken(Map.of("userId", userId), refreshTokenExpirationMs);
    }

    public Long extractUserId(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("userId", Long.class);
        } catch (JwtException e) {
            return null;
        }
    }

    public boolean isExpired(String token) {
        try {
            Date exp = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
            return exp.before(new Date());
        } catch (JwtException e) {
            return true;
        }
    }

    private String generateToken(Map<String, Object> claims, long expireTimeMs) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
