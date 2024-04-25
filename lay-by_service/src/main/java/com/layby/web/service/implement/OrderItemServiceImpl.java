package com.layby.web.service.implement;

import com.layby.domain.entity.OrderItem;
import com.layby.domain.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.layby.web.service.OrderItemService;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;


    @Override
    public void save(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }
}
