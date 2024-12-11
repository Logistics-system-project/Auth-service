package com.spring.dozen.auth.application.dto;

import com.spring.dozen.auth.domain.entity.User;

public record UserSignUpResponseDto(
        Long userId,
        String username,
        String slackId,
        String role
) {
    public static UserSignUpResponseDto from(User user) {
        return new UserSignUpResponseDto(
                user.getId(), user.getUsername(), user.getSlackId(), user.getRole().name()
        );
    }
}
