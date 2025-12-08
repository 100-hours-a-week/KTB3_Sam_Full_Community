package com.example.community.auth.userdetails;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.User;
import com.example.community.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Cacheable(value = "userDetailsCache", key = "#userId")
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));

        return new CustomUserDetails(user);
    }
}
