package com.orderservice.web.service;

import com.orderservice.domain.entity.Delivery;

public interface DeliveryService {

    Delivery findByDeliveryId(Long deliveryId);

    Long createDelivery(Long addressId);
}
