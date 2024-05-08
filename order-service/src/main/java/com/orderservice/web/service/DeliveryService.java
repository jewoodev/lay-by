package com.orderservice.web.service;

import com.orderservice.domain.entity.Delivery;

public interface DeliveryService {

    Delivery findByDeliveryId(Long deliveryId);

    Delivery findByOrderId(Long orderId);

    Long createDelivery(Long addressId, Long orderId);
}
