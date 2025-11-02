package com.example.community.service;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.User;
import com.example.community.repository.inmemory.InMemoryUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final InMemoryUserRepository inMemoryUserRepository;

    UserService(InMemoryUserRepository inMemoryUserRepository) {
        this.inMemoryUserRepository = inMemoryUserRepository;
    }

    @Transactional
    public User registerUser(String email, String password, String nickname, Long profileImageId) {
        validateEmail(email);
        validateNickname(nickname);
        return inMemoryUserRepository.save(new User(email,password, nickname, profileImageId));
    }

    @Transactional(readOnly = true)
    public User getUser(Long userId) {
        return inMemoryUserRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));
    }

    public List<User> getUserByIds(List<Long> userIds) {
        return inMemoryUserRepository.findByIds(userIds);
    }

    @Transactional
    public void modifyUser(Long userId,String nickname, Long profileImageId ) {
        User user = inMemoryUserRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));

        user.updateUser(nickname,profileImageId);
        user.recordModificationTime();
        inMemoryUserRepository.save(user);
    }

    @Transactional
    public void changePassword(Long userId, String password, String checkPassword) {
        validatePasswordInput(password, checkPassword);

        User user = inMemoryUserRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));

        user.updatePassword(password);
        inMemoryUserRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = inMemoryUserRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));

        inMemoryUserRepository.deleteById(userId);
    }


    private void validateEmail(String email) {
        if(inMemoryUserRepository.findByEmail(email).isPresent()) {
            throw new BaseException(ErrorCode.ALREADY_REGISTERED_EMAIL);
        }
    }

    private void validateNickname(String nickname) {
        if(inMemoryUserRepository.findByNickname(nickname).isPresent()) {
            throw new BaseException(ErrorCode.ALREADY_REGISTERED_NICKNAME);
        }
    }

    private void validatePasswordInput(String password, String checkPassword) {
        if(!password.equals(checkPassword)) {
            throw new BaseException(ErrorCode.INVALID_REQUEST);
        }
    }
}
