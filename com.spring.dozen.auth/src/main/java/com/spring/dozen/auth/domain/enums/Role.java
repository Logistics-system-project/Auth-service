package com.spring.dozen.auth.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    MASTER("관리자"),
    HUB_MANAGER("허브 관리자"),
    HUB_DELIVERY_STAFF("허브 배송 담당자"),
    COMPANY_DELIVERY_STAFF("업체 배송 담당자");

    private final String description;

    /**
     * 변환할 수 없는 이상한 값이 들어온 경우
     * 예외를 던지지 않고 null을 리턴하여 application 계층에서 예외를 발생시킵니다.
     */
    public static Role of(String request) {
        return switch (request) {
            case "MASTER" -> MASTER;
            case "HUB_MANAGER" -> HUB_MANAGER;
            case "HUB_DELIVERY_STAFF" -> HUB_DELIVERY_STAFF;
            case "COMPANY_DELIVERY_STAFF" -> COMPANY_DELIVERY_STAFF;
            default -> null;
        };
    }

    public boolean isNotAvailableRole() {
        return this == MASTER || this == HUB_MANAGER;
    }
}
