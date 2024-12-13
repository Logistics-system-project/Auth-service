package com.spring.dozen.auth.presentation.dto;

import com.spring.dozen.auth.application.dto.UserSignIn;

public record UserSignInRequest(
        String username,
        String password
) {
    public UserSignIn toServiceDto() {
        return new UserSignIn(username, password);
    }
}
