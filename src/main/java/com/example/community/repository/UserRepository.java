package com.example.community.repository;

import com.example.community.entity.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository {
    private Map<Long, User> userDB;
    private long sequence = 0L;

    UserRepository() {
        this.userDB = new LinkedHashMap<>();
    }

    public User save(User user) {
        user.setId(++sequence);
        userDB.put(user.getId(), user);
        return user;
    }

    public List<User> findAll() {
        return new ArrayList<>(userDB.values());
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userDB.get(id));
    }

    public void deleteById(Long id) {
        userDB.remove(id);
    }
}
