package com.example.community.auth;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenBlackList {
    private final Map<Long, String> blackList;

    TokenBlackList() {
        this.blackList = new ConcurrentHashMap<>();
    }

    public void add(Long userId, String token) {
        blackList.put(userId, token);
    }

    public boolean contains(long userId, String token) {
        String blackListedToken = blackList.get(userId);
        return blackListedToken != null && blackListedToken.equals(token);
    }

    public boolean contains(String token) {
        return blackList.values().stream().anyMatch(blackedToken -> blackedToken.equals(token));
    }
}
