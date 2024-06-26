package com.orderservice.web.service;

import com.orderservice.domain.dto.OrderStatusDto;
import com.orderservice.domain.dto.ResponseDto;
import com.orderservice.domain.dto.WishListDto;
import com.orderservice.domain.entity.Order;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {

    Long save(Order order);

    Order findByOrderId(Long orderId);

    ResponseEntity<List<OrderStatusDto>> referOrdersStatus(Long userId);

    ResponseEntity<ResponseDto> cancelOrder(Long orderId);

    ResponseEntity<ResponseDto> refundOrder(Long orderId);

    ResponseEntity<ResponseDto> makeOrder(Long userId, WishListDto wishListDto);

    List<Order> findAfterRefund();

    int getTotalPrice(Long orderId);

    ResponseEntity<String> purchaseOrder(Long orderId);
}
