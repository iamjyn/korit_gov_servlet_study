package com.korit.korit_gov_servlet_study.practice;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TodoDto2 {
    private String title;
    private String content;
    private String username;

    public Todo2 toEntity() {
        return Todo2.builder()
                .title(title)
                .content(content)
                .username(username)
                .build();
    }
}
