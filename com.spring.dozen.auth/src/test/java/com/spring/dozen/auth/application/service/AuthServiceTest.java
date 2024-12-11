package com.spring.dozen.auth.application.service;

import com.spring.dozen.auth.application.dto.UserSignUpRequestServiceDto;
import com.spring.dozen.auth.application.dto.UserSignUpResponseDto;
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
        UserSignUpRequestServiceDto request = new UserSignUpRequestServiceDto(
                "testuser",
                "password123",
                "slackId123",
                "COMPANY_DELIVERY_STAFF"
        );

        // when
        UserSignUpResponseDto response = authService.signUp(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.username()).isEqualTo(request.username());
        assertThat(response.role()).isEqualTo(Role.COMPANY_DELIVERY_STAFF.name());
    }

    @Test
    @DisplayName("회원가입 성공 - HUB_DELIVERY_STAFF 권한")
    void signUp_HubDeliveryStaffRole_Success() {
        // given
        UserSignUpRequestServiceDto request = new UserSignUpRequestServiceDto(
                "testuser",
                "password123",
                "slackId123",
                "HUB_DELIVERY_STAFF"
        );

        // when
        UserSignUpResponseDto response = authService.signUp(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.username()).isEqualTo(request.username());
        assertThat(response.role()).isEqualTo(Role.HUB_DELIVERY_STAFF.name());
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 사용자명")
    void signUp_DuplicateUsername_ThrowsException() {
        // given
        UserSignUpRequestServiceDto request = new UserSignUpRequestServiceDto(
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
        UserSignUpRequestServiceDto request = new UserSignUpRequestServiceDto(
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
        UserSignUpRequestServiceDto request = new UserSignUpRequestServiceDto(
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
        UserSignUpRequestServiceDto request = new UserSignUpRequestServiceDto(
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


}
