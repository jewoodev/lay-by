package com.layby.domain.dto.response;

import com.layby.domain.common.DeliveryStatus;
import com.layby.domain.common.OrderStatus;
import com.layby.domain.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter @Setter @Builder
@AllArgsConstructor
public class OrderResponseDto {

    private String orderStatus;
    private String deliveryStatus;
}
