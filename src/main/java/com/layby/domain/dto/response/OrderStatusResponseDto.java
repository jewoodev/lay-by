package com.layby.domain.dto.response;

import com.layby.domain.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter @Builder
@AllArgsConstructor
public class OrderStatusResponseDto {

    private String orderStatus;

    private String deliveryStatus;

    private List<String> itemNames;

    private int totalPrice;
}
