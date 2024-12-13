package com.spring.dozen.auth.domain.entity;

import com.spring.dozen.auth.domain.enums.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


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

    @Test
    @DisplayName("회원 삭제 시 삭제 관련 필드들이 정상적으로 변경되어야 한다")
    void deleteBaseTest() {
        // given
        User user = User.create(
                "testUser",
                "password",
                "slackId",
                Role.HUB_DELIVERY_STAFF
        );
        Long deletedById = 1L;

        // when
        user.deleteBase(deletedById);

        // then
        assertThat(user.getIsDeleted()).isTrue();
        assertThat(user.getDeletedBy()).isEqualTo(String.valueOf(deletedById));
        assertThat(user.getDeletedAt()).isNotNull();

        // 다른 필드들은 변경되지 않았는지 검증
        assertThat(user.getUsername()).isEqualTo("testUser");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getSlackId()).isEqualTo("slackId");
        assertThat(user.getRole()).isEqualTo(Role.HUB_DELIVERY_STAFF);
    }


}