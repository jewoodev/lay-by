package com.layby.domain.repository;

import com.layby.domain.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Delivery findByDeliveryId(Long deliveryId);
}
