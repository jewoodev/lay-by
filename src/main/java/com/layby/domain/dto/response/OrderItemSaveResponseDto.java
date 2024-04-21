package com.layby.domain.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class OrderItemSaveResponseDto extends ResponseDto {

    private OrderItemSaveResponseDto() {
        super();
    }

    public static ResponseEntity<? super OrderItemSaveResponseDto> success() {
        OrderItemSaveResponseDto responseBody = new OrderItemSaveResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
