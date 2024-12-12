package com.spring.dozen.auth.application.service;

import com.spring.dozen.auth.application.dto.UserSignIn;
import com.spring.dozen.auth.application.dto.UserSignUp;
import com.spring.dozen.auth.application.dto.UserSignUpResponse;
import com.spring.dozen.auth.application.exception.AuthException;
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
    @DisplayName("회원가입 성공 - COMPANY_DELIVERY_STAFF 권한")
    void signUp_Success() {
        // given
        UserSignUp request = new UserSignUp(
                "testuser",
                "password123",
                "slackId123",
                "COMPANY_DELIVERY_STAFF"
        );

        // when
        UserSignUpResponse response = authService.signUp(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.username()).isEqualTo(request.username());
        assertThat(response.role()).isEqualTo(Role.COMPANY_DELIVERY_STAFF.name());
    }

    @Test
    @DisplayName("회원가입 성공 - HUB_DELIVERY_STAFF 권한")
    void signUp_HubDeliveryStaffRole_Success() {
        // given
        UserSignUp request = new UserSignUp(
                "testuser",
                "password123",
                "slackId123",
                "HUB_DELIVERY_STAFF"
        );

        // when
        UserSignUpResponse response = authService.signUp(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.username()).isEqualTo(request.username());
        assertThat(response.role()).isEqualTo(Role.HUB_DELIVERY_STAFF.name());
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 사용자명")
    void signUp_DuplicateUsername_ThrowsException() {
        // given
        UserSignUp request = new UserSignUp(
                "testuser",
                "password123",
                "slackId123",
                "COMPANY_DELIVERY_STAFF"
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
        UserSignUp request = new UserSignUp(
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

    @Test
    @DisplayName("회원가입 실패 - MASTER 권한은 사용할 수 없음")
    void signUp_MasterRole_ThrowsException() {
        // given
        UserSignUp request = new UserSignUp(
                "testuser",
                "password123",
                "slackId123",
                "MASTER"
        );

        // when & then
        assertThatThrownBy(() -> authService.signUp(request))
                .isInstanceOf(AuthException.class)
                .hasMessage("접근 권한이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("회원가입 실패 - HUB_MANAGER 권한은 사용할 수 없음")
    void signUp_HubManagerRole_ThrowsException() {
        // given
        UserSignUp request = new UserSignUp(
                "testuser",
                "password123",
                "slackId123",
                "HUB_MANAGER"
        );

        // when & then
        assertThatThrownBy(() -> authService.signUp(request))
                .isInstanceOf(AuthException.class)
                .hasMessage("접근 권한이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("로그인 성공")
    void signIn_Success() {
        // given
        String username = "testuser";
        String password = "password123";
        UserSignUp signUpRequest = new UserSignUp(
                username,
                password,
                "slackId123",
                "COMPANY_DELIVERY_STAFF"
        );
        authService.signUp(signUpRequest);

        UserSignIn signInRequest = new UserSignIn(username, password);

        // when
        String token = authService.signIn(signInRequest);

        // then
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 사용자")
    void signIn_UserNotFound_ThrowsException() {
        // given
        UserSignIn signInRequest = new UserSignIn("nonexistent", "password123");

        // when & then
        assertThatThrownBy(() -> authService.signIn(signInRequest))
                .isInstanceOf(AuthException.class)
                .hasMessage("일치하는 유저 정보가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void signIn_InvalidPassword_ThrowsException() {
        // given
        String username = "testuser";
        String password = "password123";
        String wrongPassword = "wrongpassword";

        UserSignUp signUpRequest = new UserSignUp(
                username,
                password,
                "slackId123",
                "COMPANY_DELIVERY_STAFF"
        );
        authService.signUp(signUpRequest);

        UserSignIn signInRequest = new UserSignIn(username, wrongPassword);

        // when & then
        assertThatThrownBy(() -> authService.signIn(signInRequest))
                .isInstanceOf(AuthException.class)
                .hasMessage("유저 ID 또는 비밀번호 정보가 일치하지 않습니다.");
    }
}
