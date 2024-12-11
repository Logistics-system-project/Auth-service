package com.spring.dozen.auth.application.dto;

public record UserSignUpRequestServiceDto(
        String username,
        String password,
        String slackId,
        String role
) {
}
