package com.korit.korit_gov_servlet_study.ch07;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static UserRepository instance;
    private List<User> users;
    private Long userId = 1L;

    private UserRepository() {
        users = new ArrayList<>();
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    // username 찾기
    public User findByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    // List에 추가
    public User addUser(User user) {
        user.setUserId(userId++);
        users.add(user);
        return user;
    }

    // List 반환
    public List<User> getUserListAll() {
        return users;
    }
}
