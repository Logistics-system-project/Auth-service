package com.spring.dozen.auth.application.service;

import com.spring.dozen.auth.application.dto.UserUpdate;
import com.spring.dozen.auth.application.dto.UserUpdateResponse;
import com.spring.dozen.auth.application.exception.AuthException;
import com.spring.dozen.auth.domain.entity.User;
import com.spring.dozen.auth.domain.enums.Role;
import com.spring.dozen.auth.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원 정보 수정이 정상적으로 동작해야 한다")
    void updateUserTest() {
        // given
        User user = User.create(
                "testUser",
                "Pass123!",
                "oldSlackId",
                Role.HUB_DELIVERY_STAFF
        );
        User savedUser = userRepository.save(user);

        String newPassword = "New123!";
        String newSlackId = "NewSlackId";


        UserUpdate updateRequest = new UserUpdate(newPassword, newSlackId);

        // when
        UserUpdateResponse response = userService.updateUser(savedUser.getId(), updateRequest);

        // then
        User updatedUser = userRepository.findById(savedUser.getId()).orElseThrow();
        assertThat(response.username()).isEqualTo(updatedUser.getUsername());
        assertThat(response.slackId()).isEqualTo(newSlackId);
        assertThat(updatedUser.getSlackId()).isEqualTo(newSlackId);
        assertThat(passwordEncoder.matches(newPassword, updatedUser.getPassword())).isTrue();
    }

    @Test
    @DisplayName("회원 삭제가 정상적으로 동작해야 한다")
    void deleteUserTest() {
        // given
        User user = User.create(
                "testUser",
                "Pass123!",
                "slackId",
                Role.HUB_DELIVERY_STAFF
        );
        User savedUser = userRepository.save(user);
        Long masterUserId = 1L; // MASTER 권한을 가진 사용자의 ID

        // when
        userService.deleteUser(savedUser.getId(), masterUserId);

        // then
        User deletedUser = userRepository.findById(savedUser.getId()).orElseThrow();
        assertThat(deletedUser.getIsDeleted()).isTrue();
        assertThat(deletedUser.getDeletedBy()).isEqualTo(String.valueOf(masterUserId));
        assertThat(deletedUser.getDeletedAt()).isNotNull();

        // 다른 필드들은 변경되지 않았는지 검증
        assertThat(deletedUser.getUsername()).isEqualTo("testUser");
        assertThat(deletedUser.getSlackId()).isEqualTo("slackId");
        assertThat(deletedUser.getRole()).isEqualTo(Role.HUB_DELIVERY_STAFF);
    }

    @Test
    @DisplayName("존재하지 않는 회원 삭제 시 예외가 발생해야 한다")
    void deleteUserNotFoundTest() {
        // given
        Long nonExistentUserId = 999L;
        Long masterUserId = 1L;

        // when & then
        assertThatThrownBy(() -> userService.deleteUser(nonExistentUserId, masterUserId))
                .isInstanceOf(AuthException.class)
                .hasMessage("일치하는 유저 정보가 존재하지 않습니다.");
    }
}