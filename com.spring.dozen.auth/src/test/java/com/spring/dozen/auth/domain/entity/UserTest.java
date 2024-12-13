package com.spring.dozen.auth.domain.entity;

import com.spring.dozen.auth.domain.enums.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


class UserTest {

    @Test
    @DisplayName("회원 정보 수정 시 password와 slackId가 정상적으로 변경되어야 한다")
    void updateTest() {
        // given
        User user = User.create(
                "testUser",
                "oldPassword",
                "oldSlackId",
                Role.HUB_DELIVERY_STAFF
        );

        String newPassword = "newPassword";
        String newSlackId = "newSlackId";

        // when
        user.update(newPassword, newSlackId);

        // then
        assertThat(user.getPassword()).isEqualTo(newPassword);
        assertThat(user.getSlackId()).isEqualTo(newSlackId);

        // 다른 필드들은 변경되지 않았는지 검증
        assertThat(user.getUsername()).isEqualTo("testUser");
        assertThat(user.getRole()).isEqualTo(Role.HUB_DELIVERY_STAFF);
    }

}