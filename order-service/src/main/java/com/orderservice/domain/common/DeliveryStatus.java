package com.orderservice.domain.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryStatus {
    PREPARE("상품 준비중"),
    PROCESS("배송 중"),
    COMPLETE("배송 완료");

    private final String description;
}
