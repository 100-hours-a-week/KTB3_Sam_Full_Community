package com.example.community.service;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.User;
import com.example.community.event.UserDeletedEvent;
import com.example.community.event.UserSavedEvent;
import com.example.community.repository.UserRepository;
import com.example.community.repository.inmemory.InMemoryUserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;

    UserService(UserRepository userRepository, ApplicationEventPublisher eventPublisher, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(String email, String password, String nickname, Long profileImageId) {
        validateEmail(email);
        validateNickname(nickname);

        String encodedPassword = passwordEncoder.encode(password);
        User user = userRepository.save(new User(email,encodedPassword, nickname));

        eventPublisher.publishEvent(new UserSavedEvent(user.getId(), profileImageId));

        return user;
    }

    @Transactional(readOnly = true)
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));
    }

    @Transactional
    public void modifyUser(Long userId,String nickname, Long profileImageId ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));

        user.updateUser(nickname);
        userRepository.save(user);

        eventPublisher.publishEvent(new UserSavedEvent(user.getId(), profileImageId));
    }

    @Transactional
    public void changePassword(Long userId, String password, String checkPassword) {
        validatePasswordInput(password, checkPassword);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));

        String encodedPassword = passwordEncoder.encode(password);
        user.updatePassword(encodedPassword);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));
        eventPublisher.publishEvent(new UserDeletedEvent(userId));
        userRepository.deleteById(userId);
    }

    @Transactional(readOnly = true)
    public Boolean checkEmailDuplicated(String email) {
        return userRepository.existByEmail(email);
    }

    @Transactional(readOnly = true)
    public Boolean checkNicknameDuplicated(String nickname) {
        return userRepository.existByNickname(nickname);
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
