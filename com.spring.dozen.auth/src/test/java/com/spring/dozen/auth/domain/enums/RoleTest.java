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

}