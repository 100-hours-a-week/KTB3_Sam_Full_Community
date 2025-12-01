package com.example.community.auth.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class TokenProvider {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final TokenBlackList tokenBlackList;

    TokenProvider(JwtUtil jwtUtil, UserDetailsService userDetailsService, TokenBlackList tokenBlackList) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.tokenBlackList = tokenBlackList;
    }

    public Authentication getAuthentication(String token) {
        Long userId = jwtUtil.extractUserId(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(userId.toString());

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public boolean validateToken(String token) {
        if(token == null) {
            return false;
        }

        Long userId = jwtUtil.extractUserId(token);

        boolean isBlackList = tokenBlackList.contains(userId, token);
        boolean isExpired = jwtUtil.isExpired(token);

        return !isBlackList && !isExpired;
    }
}
