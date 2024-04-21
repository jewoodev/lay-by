package com.layby.domain.common;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DeliveryStatus {
    PREPARE("상품 준비중"),
    PROCESS("배송 중"),
    COMPLETE("배송 완료");

    private final String description;
}
