package com.korit.korit_gov_servlet_study.practice;

import com.korit.korit_gov_servlet_study.ch03.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository2 {
    private static UserRepository2 instance;
    private List<User2> users;
    private Integer userId = 1;

    private UserRepository2() {
        users = new ArrayList<>();
    }

    public static UserRepository2 getInstance() {
        if (instance == null) {
            instance = new UserRepository2();
        }
        return instance;
    }

    public User2 findByUsername(String username) {
        return users.stream().filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public User2 addUSer(User2 user) {
        user.setUserId(userId++);
        users.add(user);
        return user;
    }

    public List<User2> findAll() {
        return users;
    }



}
