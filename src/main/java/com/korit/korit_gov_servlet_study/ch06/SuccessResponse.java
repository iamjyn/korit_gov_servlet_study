package com.korit.korit_gov_servlet_study.ch06;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SuccessResponse<T> {
    private String message;
    private T body;
}
