package com.orderservice.web.service.implement;

import com.orderservice.domain.entity.OrderItem;
import com.orderservice.domain.repository.OrderItemRepository;
import com.orderservice.web.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    @Override
    public void save(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }

    @Override
    public OrderItem findByOrderItemId(Long orderItemId) {
        return orderItemRepository.findByOrderItemId(orderItemId);
    }

    @Override
    public List<OrderItem> findAllByOrderId(Long orderId) {
        return orderItemRepository.findAllByOrderId(orderId);
    }
}
