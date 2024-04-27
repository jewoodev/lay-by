package com.orderservice.domain.dto;

import com.orderservice.domain.common.DeliveryStatus;
import com.orderservice.domain.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter @Builder
@AllArgsConstructor
public class OrderStatusDto {

    private String orderStatus;

    private String deliveryStatus;

    private List<String> itemNames;

    private int totalPrice;

    //== 변환 메서드 ==//
    public static OrderStatusDto convertToStatusDto(Order order, DeliveryStatus deliveryStatus) {

        // 배송 정보는 checkStatus() 로 업데이트해서 response
        OrderStatusDto orderStatusDto = OrderStatusDto.builder()
                .orderStatus(order.getOrderStatus().getDescription())
                .deliveryStatus(deliveryStatus.getDescription())
                .totalPrice(order.getTotalPrice())
                .build();

        return orderStatusDto;
    }
}
