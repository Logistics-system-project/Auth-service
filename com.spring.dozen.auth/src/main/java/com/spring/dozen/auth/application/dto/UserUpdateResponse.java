package com.spring.dozen.auth.application.dto;

import com.spring.dozen.auth.domain.entity.User;

public record UserUpdateResponse(
        Long userId,
        String username,
        String slackId,
        String role
) {
    public static UserUpdateResponse from(User user) {
        return new UserUpdateResponse(
                user.getId(), user.getUsername(), user.getSlackId(), user.getRole().name()
        );
    }
}
