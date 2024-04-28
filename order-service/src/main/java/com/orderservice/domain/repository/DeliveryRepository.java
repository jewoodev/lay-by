package com.orderservice.domain.repository;

import com.orderservice.domain.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Delivery findByDeliveryId(Long deliveryId);
}
