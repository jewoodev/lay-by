package com.layby.web.controller;

import com.layby.domain.dto.response.OrderStatusResponseDto;
import com.layby.domain.dto.response.ResponseDto;
import com.layby.web.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<List<OrderStatusResponseDto>> referOrdersStatus(Authentication authentication) {

        return orderService.referOrdersStatus(authentication);
    }

    @PatchMapping("/{order_id}/cancel")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<ResponseDto> cancelOrder(
            @PathVariable(name = "order_id") Long orderId
    ) {
        return orderService.cancelOrder(orderId);
    }

    @PatchMapping("/{order_id}/refund")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<ResponseDto> refundOrder(
            @PathVariable(name = "order_id") Long orderId
    ) {
        return orderService.refundOrder(orderId);
    }
}
