package com.layby.domain.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    ORDER("주문 완료"),
    CANCEL("주문 취소"),
    REFUND_PROCESS("반품 처리중"),
    REFUND_COMPLETE("반품 완료");

    private final String description;
}
