package com.example.community.service;

import com.example.community.auth.JwtUtil;
import com.example.community.auth.TokenBlackList;
import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.dto.AuthToken;
import com.example.community.entity.User;
import com.example.community.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final TokenBlackList tokenBlackList;

    AuthService(UserRepository userRepository, JwtUtil jwtUtil, TokenBlackList tokenBlackList) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.tokenBlackList = tokenBlackList;
    }

    @Transactional
    public AuthToken login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));
        validatePassword(user, password);

        return new AuthToken(jwtUtil.generateAccessToken(user.getId()), jwtUtil.generateRefreshToken(user.getId()));
    }

    @Transactional
    public void logout(Long userId, String token) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));

        tokenBlackList.add(userId, token);
    }

    private void validatePassword(User user, String password) {
        if(!user.getPassword().equals(password)) {
            throw new BaseException(ErrorCode.INVALID_PASSWORD);
        }
    }
}
