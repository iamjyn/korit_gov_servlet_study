package com.korit.korit_gov_servlet_study.ch06;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Response {
    private String message;
}
