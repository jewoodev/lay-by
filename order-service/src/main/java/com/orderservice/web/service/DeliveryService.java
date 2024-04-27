package com.orderservice.web.service;


import com.orderservice.domain.entity.Delivery;

public interface DeliveryService {

    Long save(Delivery delivery);

    Long saveByAddressId(Long addressId);

    Delivery findByOrderId(Long orderId);

    Delivery findByDeliveryId(Long deliveryId);
}