package com.spring.dozen.auth.application.dto;

import org.springframework.http.HttpStatus;

/**
 * 공통 DTO
 * */
public record ApiResponse<T>(HttpStatus status, String message, T data) {
    public static <T> ApiResponse<T> success(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }
}
