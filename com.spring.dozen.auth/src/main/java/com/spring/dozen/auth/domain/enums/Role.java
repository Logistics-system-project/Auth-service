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
}
