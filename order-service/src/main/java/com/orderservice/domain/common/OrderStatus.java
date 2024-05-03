package com.orderservice.domain.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    NOT_PURCHASE("결제 대기"),
    PURCHASE("결제 완료"),
    CANCEL("주문 취소"),

    REFUND_PROCESS("반품 처리중"),
    REFUND_COMPLETE("반품 완료");

    private final String description;
}
