package com.example.community.repository;

import com.example.community.entity.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UserRepository {
    private Map<Long, User> userDB;
    private long sequence = 0L;

    UserRepository() {
        this.userDB = new LinkedHashMap<>();
    }

    public User save(User user) {
        if(user.getId() == null) {
            user.setId(++sequence);
        }
        userDB.put(user.getId(), user);
        return user;
    }

    public List<User> findAll() {
        return new ArrayList<>(userDB.values());
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userDB.get(id));
    }

    public List<User> findByIds(List<Long> ids) {
        return ids.stream()
                .map(userDB::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Optional<User> findByEmail(String email) {
        return userDB.values().stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst();
    }

    public Optional<User> findByNickname(String nickname) {
        return userDB.values().stream()
                .filter(user -> nickname.equals(user.getNickname()))
                .findFirst();
    }

    public void deleteById(Long id) {
        userDB.remove(id);
    }
}
