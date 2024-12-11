package com.spring.dozen.auth.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 공통 DTO
 * */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T> (
        String message,
        T data
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("API 요청에 성공했습니다", data);
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>("API 요청에 성공했습니다", null);
    }
}
