package com.layby.web.service;

import com.layby.domain.dto.response.OrderStatusResponseDto;
import com.layby.domain.entity.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface OrderService {

    void save(Order order);

    ResponseEntity<List<OrderStatusResponseDto>> referOrdersStatus(Authentication authentication);
}
