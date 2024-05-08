package com.orderservice.web.controller;

import com.orderservice.domain.dto.OrderStatusDto;
import com.orderservice.domain.dto.ResponseDto;
import com.orderservice.domain.dto.WishListDto;
import com.orderservice.web.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    /**
     사용자의 주문했던 Order 들의 상태를 조회하는 API
     */
    @GetMapping("/{user_id}/all")
    public ResponseEntity<List<OrderStatusDto>> referOrdersStatus(
            @PathVariable(name = "user_id") Long userId
    ) {
        return orderService.referOrdersStatus(userId);
    }

    /**
     전달 받은 위시리스트로 주문을 생성하는 API
     */
    @PostMapping("/{user_id}")
    public ResponseEntity<ResponseDto> makeOrder(
            @PathVariable(name = "user_id") Long userId,
            @RequestBody WishListDto wishListDto
    ) {
        return orderService.makeOrder(userId, wishListDto);
    }

    /**
     결제 페이지에서 주소를 선택한 후 최종적으로 호출하는 결제 API
     */
    @PutMapping("/{order_id}")
    public ResponseEntity<String> purchaseOrder(
            @PathVariable(name = "order_id") Long orderId
    ) {
        return orderService.purchaseOrder(orderId);
    }

    /**
     주문을 취소하는 API
     */
    @PutMapping("/{user_id}/{order_id}/cancel")
    public ResponseEntity<ResponseDto> cancelOrder(
            @PathVariable(name = "order_id") Long orderId
    ) {
        return orderService.cancelOrder(orderId);
    }

    /**
     주문을 환불하는 API
     */
    @PutMapping("/{order_id}/refund")
    public ResponseEntity<ResponseDto> refundOrder(
            @PathVariable(name = "order_id") Long orderId
    ) {
        return orderService.refundOrder(orderId);
    }
}
