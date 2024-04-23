package com.layby.web.service;

import com.layby.domain.dto.response.OrderStatusResponseDto;
import com.layby.domain.dto.response.ResponseDto;
import com.layby.domain.entity.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface OrderService {

    void save(Order order);

    Order findByOrderId(Long orderId);

    List<Order> findAtferRefund();

    ResponseEntity<List<OrderStatusResponseDto>> referOrdersStatus(Authentication authentication);

    ResponseEntity<ResponseDto> cancelOrder(Long orderId);

    ResponseEntity<ResponseDto> refundOrder(Long orderId);
}
