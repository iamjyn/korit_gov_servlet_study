package com.korit.korit_gov_servlet_study.practice;

import lombok.Data;

@Data
public class UserDto2 {
    private String username;
    private String password;
    private String name;
    private String email;

    public User2 toEntity() {
        return User2.builder()
                .username(username)
                .password(password)
                .name(name)
                .email(email)
                .build();
    }
}
