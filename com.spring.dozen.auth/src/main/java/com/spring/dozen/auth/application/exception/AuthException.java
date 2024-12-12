package com.spring.dozen.auth.application.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String message;

    public AuthException(AuthErrorCode authErrorCode) {
        this.httpStatus = authErrorCode.getHttpStatus();
        this.message = authErrorCode.getMessage();
    }
}
