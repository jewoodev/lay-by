package com.orderservice.web.service.implement;

import com.orderservice.domain.entity.Delivery;
import com.orderservice.web.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orderservice.domain.repository.DeliveryRepository;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Override
    @Transactional
    public Long save(Delivery delivery) {
        Delivery saved = deliveryRepository.save(delivery);
        return saved.getDeliveryId();
    }


    @Override
    @Transactional
    public Long saveByAddressId(Long addressId) {
        Delivery delivery = new Delivery(addressId);
        Delivery saved = deliveryRepository.save(delivery);
        return saved.getDeliveryId();
    }

    @Override
    public Delivery findByOrderId(Long orderId) {
        return deliveryRepository.findByOrderId(orderId);
    }

    @Override
    public Delivery findByDeliveryId(Long deliveryId) {
        return deliveryRepository.findByDeliveryId(deliveryId);
    }
}
