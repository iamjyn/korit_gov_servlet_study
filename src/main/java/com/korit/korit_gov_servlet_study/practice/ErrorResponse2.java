package com.korit.korit_gov_servlet_study.practice;

import lombok.Builder;

@Builder
public class ErrorResponse2 {
    private Integer status = 400;
    private String massage;
}
