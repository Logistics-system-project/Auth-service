package com.spring.dozen.auth.application.dto;

public record UserSignUp(
        String username,
        String password,
        String slackId,
        String role
) {
}
