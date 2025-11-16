package com.korit.korit_gov_servlet_study.practice;

import lombok.Builder;

@Builder
public class SuccessResponse2<T> {
    private Integer status = 200;
    private String massage;
    private T body;
}
