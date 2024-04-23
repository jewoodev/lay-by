package com.layby.web.service.implement;

import com.layby.domain.entity.Delivery;
import com.layby.domain.repository.DeliveryRepository;
import com.layby.web.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Override
    @Transactional
    public void save(Delivery delivery) {
        deliveryRepository.save(delivery);
    }
}
