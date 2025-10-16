package com.example.community.service;

import com.example.community.entity.User;
import com.example.community.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String email, String password, String nickname, int profileImageId) {
        return userRepository.save(new User(email,password, nickname, profileImageId));
    }
}
