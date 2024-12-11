package com.spring.dozen.auth.application.service;

import com.spring.dozen.auth.application.dto.UserSignUpRequestDto;
import com.spring.dozen.auth.application.dto.UserSignUpResponseDto;
import com.spring.dozen.auth.application.exception.AuthErrorCode;
import com.spring.dozen.auth.application.exception.AuthException;
import com.spring.dozen.auth.application.interfaces.TokenProvider;
import com.spring.dozen.auth.domain.entity.User;
import com.spring.dozen.auth.domain.enums.Role;
import com.spring.dozen.auth.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    /**
     * 사용자 등록
     *
     * @param userId 사용자 ID
     * @return 저장된 사용자
     * @Param username 사용자명
     * @Param password 비밀번호
     * @Param role 권한
     */
    @Transactional
    public UserSignUpResponseDto signUp(UserSignUpRequestDto signUpRequest) {
        // userId 중복 체크
        if (userRepository.findByUsername(signUpRequest.username()).isPresent()) {
            throw new AuthException(AuthErrorCode.DUPLICATE_USERNAME);
        }

        // application.service 에서 예외를 처리합니다.
        Role role = Role.of(signUpRequest.role());
        if (role == null) {
            throw new AuthException(AuthErrorCode.UNSUPPORTED_ROLE);
        }

        User user = User.create(
                signUpRequest.username(),
                passwordEncoder.encode(signUpRequest.password()),
                signUpRequest.slackId(),
                role);
        return UserSignUpResponseDto.from(userRepository.save(user));
    }
}
