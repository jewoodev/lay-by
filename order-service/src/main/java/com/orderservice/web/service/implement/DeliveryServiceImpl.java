package com.orderservice.web.service.implement;

import com.orderservice.domain.entity.Delivery;
import com.orderservice.web.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orderservice.domain.repository.DeliveryRepository;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Override
    public Delivery findByDeliveryId(Long deliveryId) {
        return deliveryRepository.findByDeliveryId(deliveryId);
    }

    @Override
    @Transactional
    public Long createDelivery(Long addressId) {
        Delivery delivery = new Delivery(addressId);
        Delivery saved = deliveryRepository.save(delivery);
        return saved.getDeliveryId();
    }
}
