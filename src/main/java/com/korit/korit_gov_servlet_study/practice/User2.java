package com.korit.korit_gov_servlet_study.practice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User2 {
    private Integer userId;
    private String username;
    private String password;
    private String name;
    private String email;
}
