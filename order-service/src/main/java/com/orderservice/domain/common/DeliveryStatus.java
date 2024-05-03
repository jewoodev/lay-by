package com.orderservice.domain.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryStatus {
    PREPARE("상품 준비중"),
    PROCESS("배송 중"),
    COMPLETE("배송 완료"),
    CANCEL("배송 취소"),

    RETURN_PROCESS("반품 상품 수거 중"),
    RETURN_COMPLETE("판매자에게 도착");

    private final String description;
}
