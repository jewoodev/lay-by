package com.layby.domain.common;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DeliveryStatus {
    PREPARE("상품 준비중"),
    PROCESS("배송 중"),
    COMPLETE("반품 처리중");

    private final String description;
}
