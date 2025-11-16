package com.korit.korit_gov_servlet_study.ch07;

import java.util.List;

public class UserService {
    private static UserService instance;
    private UserRepository userRepository;

    private UserService() {
        userRepository = UserRepository.getInstance();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    // 중복 검사
    public boolean isDuplicatedUsername(String username) {
        return userRepository.findByUsername(username) != null;
    }

    // add
    public User addUser(User user) {
        return userRepository.addUser(user);
    }

    // 단건 조회
    public User getUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // 전체 조회
    public List<User> getAllUsers() {
        return userRepository.allUsers();
    }
}
