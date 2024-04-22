package com.layby.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
@AllArgsConstructor
public class OrderStatusResponseDto {

    private String orderStatus;
    private String deliveryStatus;
}
