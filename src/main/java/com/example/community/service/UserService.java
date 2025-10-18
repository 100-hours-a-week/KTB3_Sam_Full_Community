package com.example.community.service;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.User;
import com.example.community.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String email, String password, String nickname, Long profileImageId) {
        validateEmail(email);
        validateNickname(nickname);
        return userRepository.save(new User(email,password, nickname, profileImageId));
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));
    }

    public List<User> getUserByIds(List<Long> userIds) {
        return userRepository.findByIds(userIds);
    }

    public void modifyUser(Long userId,String nickname, Long profileImageId ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));

        user.updateUser(nickname,profileImageId);
        userRepository.save(user);
    }

    public void changePassword(Long userId, String password, String checkPassword) {
        validatePasswordInput(password, checkPassword);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));

        user.updatePassword(password);
        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));

        userRepository.deleteById(userId);
    }


    private void validateEmail(String email) {
        if(userRepository.findByEmail(email).isPresent()) {
            throw new BaseException(ErrorCode.ALREADY_REGISTERED_EMAIL);
        }
    }

    private void validateNickname(String nickname) {
        if(userRepository.findByNickname(nickname).isPresent()) {
            throw new BaseException(ErrorCode.ALREADY_REGISTERED_NICKNAME);
        }
    }

    private void validatePasswordInput(String password, String checkPassword) {
        if(!password.equals(checkPassword)) {
            throw new BaseException(ErrorCode.INVALID_REQUEST);
        }
    }
}
