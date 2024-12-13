package com.spring.dozen.auth.application.service;

import com.spring.dozen.auth.application.dto.UserSignIn;
import com.spring.dozen.auth.application.dto.UserSignUp;
import com.spring.dozen.auth.application.dto.UserSignUpResponse;
import com.spring.dozen.auth.application.exception.AuthErrorCode;
import com.spring.dozen.auth.application.exception.AuthException;
import com.spring.dozen.auth.application.interfaces.TokenProvider;
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
    private final TokenProvider tokenProvider;

    /**
     * 사용자 등록 - COMPANY_DELIVERY_STAFF, HUB_DELIVERY_STAFF 전용
     *
     * @return 저장된 사용자
     * @Param UserSignUp - username, password, slackId, role
     */
    @Transactional
    public UserSignUpResponse signUp(UserSignUp signUpRequest) {
        log.info("signUp.UserSignUp: {}", signUpRequest);

        Role role = validateAndGetRole(signUpRequest);

        // 권한이 HUB_MANAGER, MASTER 인 경우 예외
        if (role.isNotDeliveryStaffRole()) {
            throw new AuthException(AuthErrorCode.FORBIDDEN_ACCESS);
        }

        User user = User.create(
                signUpRequest.username(),
                passwordEncoder.encode(signUpRequest.password()),
                signUpRequest.slackId(),
                role);
        return UserSignUpResponse.from(userRepository.save(user));
    }

    /**
     * 사용자 등록 - HUB_MANAGER 전용
     *
     * @return 저장된 사용자
     * @Param UserSignUp - username, password, slackId, role
     */
    @Transactional
    public UserSignUpResponse signUpForHubManager(UserSignUp signUpRequest) {
        log.info("signUpForHubManager.UserSignUp: {}", signUpRequest);

        Role role = validateAndGetRole(signUpRequest);

        // 권한이 HUB_MANAGER 가 아닌 경우 예외
        if (role.isNotHubManager()) {
            throw new AuthException(AuthErrorCode.FORBIDDEN_ACCESS);
        }

        User user = User.create(
                signUpRequest.username(),
                passwordEncoder.encode(signUpRequest.password()),
                signUpRequest.slackId(),
                role);
        return UserSignUpResponse.from(userRepository.save(user));
    }

    /**
     * 사용자 인증
     *
     * @return JWT 액세스 토큰
     * @Param UserSignIn - username password
     */
    public String signIn(UserSignIn signInRequest) {
        log.info("signIn.UserSignIn: {}", signInRequest);
        User user = userRepository.findByUsername(signInRequest.username())
                .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(signInRequest.password(), user.getPassword())) {
            throw new AuthException(AuthErrorCode.INVALID_PASSWORD);
        }

        return tokenProvider.createAccessToken(user.getId(), user.getRole());
    }

    /**
     * 회원 존재 여부 검증 비즈니스 로직
     */
    public Boolean verifyUser(Long id) {
        log.info("verifyUser.userId: {}", id);
        return userRepository.findById(id).isPresent();
    }

    private Role validateAndGetRole(UserSignUp signUpRequest) {
        if (userRepository.findByUsername(signUpRequest.username()).isPresent()) {
            throw new AuthException(AuthErrorCode.DUPLICATE_USERNAME);
        }

        Role role = Role.of(signUpRequest.role());
        if (role == null) {
            throw new AuthException(AuthErrorCode.UNSUPPORTED_ROLE);
        }
        return role;
    }
}
