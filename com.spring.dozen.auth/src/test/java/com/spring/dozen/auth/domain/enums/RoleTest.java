package com.spring.dozen.auth.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class RoleTest {

    @DisplayName("유효한 권한이름이 입력되면 Role 로 변환시킨다.")
    @ParameterizedTest
    @ValueSource(strings = {"MASTER", "HUB_MANAGER", "HUB_DELIVERY_STAFF", "COMPANY_DELIVERY_STAFF"})
    void of_ValidRole_ReturnsCorrectEnum(String input) {
        // when
        Role result = Role.of(input);

        // then
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(input);
    }

    @DisplayName("유효하지 않은 입력값의 경우 null 을 반환시킨다.")
    @ParameterizedTest
    @ValueSource(strings = {"INVALID_ROLE", "random", ""})
    void of_InvalidRole_ReturnsNull(String input) {
        // when
        Role result = Role.of(input);

        // then
        assertThat(result).isNull();
    }

    @DisplayName("isNotDeliveryStaffRole()은 MASTER, HUB_MANAGER 권한의 경우 true를 리턴한다.")
    @ParameterizedTest
    @ValueSource(strings = {"MASTER", "HUB_MANAGER"})
    void isNotDeliveryStaffRole_ReturnsTrueForForbiddenRoles(String input) {
        // given
        Role role = Role.of(input);

        // when & then
        assertThat(role.isNotDeliveryStaffRole()).isTrue();
    }

    @DisplayName("isNotDeliveryStaffRole()은 HUB_DELIVERY_STAFF, COMPANY_DELIVERY_STAFF 권한의 경우 false를 리턴한다.")
    @ParameterizedTest
    @ValueSource(strings = {"HUB_DELIVERY_STAFF", "COMPANY_DELIVERY_STAFF"})
    void isNotDeliveryStaffRole_ReturnsFalseForAllowedRoles(String input) {
        // given
        Role role = Role.of(input);

        // when & then
        assertThat(role.isNotDeliveryStaffRole()).isFalse();
    }

    @DisplayName("isNotHubManager()은 HUB_MANAGER가 아닌 권한들은 true를 반환한다")
    @ParameterizedTest
    @ValueSource(strings = {"MASTER", "HUB_DELIVERY_STAFF", "COMPANY_DELIVERY_STAFF"})
    void isNotHubManager_ReturnsTrueForNonHubManagerRoles(String input) {
        // given
        Role role = Role.of(input);

        // when & then
        assertThat(role.isNotHubManager()).isTrue();
    }

    @DisplayName("isNotHubManager()은 HUB_MANAGER 권한의 경우 false를 반환한다")
    @ParameterizedTest
    @ValueSource(strings = {"HUB_MANAGER"})
    void isNotHubManager_ReturnsFalseForHubManagerRole(String input) {
        // given
        Role role = Role.of(input);

        // when & then
        assertThat(role.isNotHubManager()).isFalse();
    }
}