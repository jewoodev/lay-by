package com.orderservice.web.controller;

import com.orderservice.domain.dto.AddressDto;
import com.orderservice.domain.dto.OrderStatusDto;
import com.orderservice.domain.dto.ResponseDto;
import com.orderservice.domain.dto.WishItemDto;
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
@RequestMapping("/")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{user_id}/all")
    public ResponseEntity<List<OrderStatusDto>> referOrdersStatus(
            @PathVariable(name = "user_id") Long userId
    ) {
        return orderService.referOrdersStatus(userId);
    }

    @PostMapping("/{user_id}")
    public ResponseEntity<ResponseDto> purchaseWishList(
            @PathVariable(name = "user_id") Long userId,
            @RequestBody AddressDto addressDto,
            WishListDto dto
    ) {
        Long addressId = addressDto.getAddressId();
        return orderService.purchaseWishList(userId, addressId, dto);
    }

    @PatchMapping("/{user_id}/{order_id}/cancel")
    public ResponseEntity<ResponseDto> cancelOrder(
            @PathVariable(name = "order_id") Long orderId
    ) {
        return orderService.cancelOrder(orderId);
    }

    @PatchMapping("/{order_id}/refund")
    public ResponseEntity<ResponseDto> refundOrder(
            @PathVariable(name = "order_id") Long orderId
    ) {
        return orderService.refundOrder(orderId);
    }
}
