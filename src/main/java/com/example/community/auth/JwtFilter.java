package com.example.community.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtFilter implements Filter {
    private final JwtUtil jwtUtil;
    private final ExcludePathMatcher excludePathMatcher;
    private final TokenBlackList tokenBlackList;

    public JwtFilter(JwtUtil jwtUtil, ExcludePathMatcher excludePathMatcher, TokenBlackList tokenBlackList) {
        this.jwtUtil = jwtUtil;
        this.excludePathMatcher = excludePathMatcher;
        this.tokenBlackList = tokenBlackList;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if(excludePathMatcher.isExcluded(req)) {
            chain.doFilter(request,response);
            return;
        }

        String header = req.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = header.substring(7);
        Long userId = jwtUtil.extractUserId(token);

        if (userId == null || jwtUtil.isExpired(token)) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if(tokenBlackList.contains(userId,token)) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        req.setAttribute("accessToken", token);

        chain.doFilter(request, response);
    }
}
