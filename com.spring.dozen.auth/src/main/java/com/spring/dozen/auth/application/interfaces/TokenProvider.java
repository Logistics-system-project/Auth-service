package com.spring.dozen.auth.application.interfaces;

import com.spring.dozen.auth.domain.enums.Role;

public interface TokenProvider {
    String createAccessToken(Long userId, Role role);
}
