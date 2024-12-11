package com.spring.dozen.auth.application.service;

import com.spring.dozen.auth.application.dto.UserSignUpRequestDto;
import com.spring.dozen.auth.application.dto.UserSignUpResponseDto;
import com.spring.dozen.auth.application.exception.AuthException;
import com.spring.dozen.auth.domain.entity.User;
import com.spring.dozen.auth.domain.enums.Role;
import com.spring.dozen.auth.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원가입 성공")
    void signUp_Success() {
        // given
        UserSignUpRequestDto request = new UserSignUpRequestDto(
                "testuser",
                "password123",
                "slackId123",
                "HUB_MANAGER"
        );

        User savedUser = User.create(
                request.username(),
                "encodedPassword",
                request.slackId(),
                Role.HUB_MANAGER
        );

        // when
        UserSignUpResponseDto response = authService.signUp(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.username()).isEqualTo(request.username());
        assertThat(response.role()).isEqualTo(Role.HUB_MANAGER.name());
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 사용자명")
    void signUp_DuplicateUsername_ThrowsException() {
        // given
        UserSignUpRequestDto request = new UserSignUpRequestDto(
                "testuser",
                "password123",
                "slackId123",
                "HUB_MANAGER"
        );

        // 먼저 동일한 username으로 사용자 생성
        authService.signUp(request);

        // when & then
        assertThatThrownBy(() -> authService.signUp(request))
                .isInstanceOf(AuthException.class)
                .hasMessage("유저 이름이 이미 존재합니다.");
    }

    @Test
    @DisplayName("회원가입 실패 - 잘못된 역할")
    void signUp_InvalidRole_ThrowsException() {
        // given
        UserSignUpRequestDto request = new UserSignUpRequestDto(
                "testuser",
                "password123",
                "slackId123",
                "INVALID_ROLE"
        );

        // when & then
        assertThatThrownBy(() -> authService.signUp(request))
                .isInstanceOf(AuthException.class)
                .hasMessage("존재하지 않는 권한 이름입니다.");
    }
}
