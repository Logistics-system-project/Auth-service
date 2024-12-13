package com.spring.dozen.auth.application.service;

import com.spring.dozen.auth.application.dto.UserUpdate;
import com.spring.dozen.auth.application.dto.UserUpdateResponse;
import com.spring.dozen.auth.application.exception.AuthErrorCode;
import com.spring.dozen.auth.application.exception.AuthException;
import com.spring.dozen.auth.domain.entity.User;
import com.spring.dozen.auth.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 유저 조회, 수정, 삭제
 * */
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserUpdateResponse updateUser(Long userId, UserUpdate updateRequest) {
        log.info("updateUser.userId: {}, UserUpdate: {}", userId, updateRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND));
        user.update(passwordEncoder.encode(updateRequest.password()), updateRequest.slackId());
        return UserUpdateResponse.from(user);
    }

    @Transactional
    public void deleteUser(Long userId, Long requestUserId) {
        // requestUserId 는 MASTER 의 ID 밖에 들어올 수 없습니다. 삭제는 MASTER 만 가능해서
        log.info("deleteUser.userId: {}, requsetUserId: {}", userId, requestUserId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND));
        user.deleteBase(requestUserId);
    }
}
