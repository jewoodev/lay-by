package com.orderservice.web.controller;

import com.orderservice.web.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping("/{address_id}/{order_id}")
    public Long createDelivery(
            @PathVariable(name = "address_id") Long addressId,
            @PathVariable(name = "order_id") Long orderId
    ) {
        return deliveryService.createDelivery(addressId, orderId);
    }
}
