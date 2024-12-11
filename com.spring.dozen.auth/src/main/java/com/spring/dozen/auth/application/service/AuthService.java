package com.spring.dozen.auth.application.service;

import com.spring.dozen.auth.application.dto.UserSignUpRequestDto;
import com.spring.dozen.auth.application.dto.UserSignUpResponseDto;
import com.spring.dozen.auth.application.exception.AuthErrorCode;
import com.spring.dozen.auth.application.exception.AuthException;
import com.spring.dozen.auth.domain.entity.User;
import com.spring.dozen.auth.domain.enums.Role;
import com.spring.dozen.auth.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 사용자 등록 - COMPANY_DELIVERY_STAFF, HUB_DELIVERY_STAFF 전용
     *
     * @param userId 사용자 ID
     * @return 저장된 사용자
     * @Param username 사용자명
     * @Param password 비밀번호
     * @Param role 권한
     */
    @Transactional
    public UserSignUpResponseDto signUp(UserSignUpRequestDto signUpRequest) {
        log.info("AuthService.signUp.UserSignUpRequestDto: {}", signUpRequest);

        // username 중복 체크
        if (userRepository.findByUsername(signUpRequest.username()).isPresent()) {
            throw new AuthException(AuthErrorCode.DUPLICATE_USERNAME);
        }

        // String role -> Role role 변환 및 유효성 검사
        // application.service 에서 예외를 처리합니다.
        Role role = Role.of(signUpRequest.role());
        if (role == null) {
            throw new AuthException(AuthErrorCode.UNSUPPORTED_ROLE);
        }

        // 권한이 HUB_MANAGER, MASTER 인 경우 예외
        if (role.isNotAvailableRole()) {
            throw new AuthException(AuthErrorCode.FORBIDDEN_ACCESS);
        }

        User user = User.create(
                signUpRequest.username(),
                passwordEncoder.encode(signUpRequest.password()),
                signUpRequest.slackId(),
                role);
        return UserSignUpResponseDto.from(userRepository.save(user));
    }
}
