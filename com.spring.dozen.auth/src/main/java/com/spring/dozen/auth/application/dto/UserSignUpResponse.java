package com.spring.dozen.auth.application.dto;

import com.spring.dozen.auth.domain.entity.User;

public record UserSignUpResponse(
        Long userId,
        String username,
        String slackId,
        String role
) {
    public static UserSignUpResponse from(User user) {
        return new UserSignUpResponse(
                user.getId(), user.getUsername(), user.getSlackId(), user.getRole().name()
        );
    }
}
