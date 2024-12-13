package com.spring.dozen.auth.application.service;

import com.spring.dozen.auth.application.dto.UserUpdate;
import com.spring.dozen.auth.application.dto.UserUpdateResponse;
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
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        assertThat(passwordEncoder.matches(newPassword, updatedUser.getPassword())).isTrue();
    }

}