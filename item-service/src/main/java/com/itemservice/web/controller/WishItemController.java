package com.itemservice.web.controller;

import com.itemservice.domain.dto.AddressDto;
import com.itemservice.domain.dto.ResponseDto;
import com.itemservice.domain.dto.WishItemDto;
import com.itemservice.domain.dto.WishListDto;
import com.itemservice.web.service.WishItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/wish-item")
public class WishItemController {

    private final WishItemService wishItemService;

    @GetMapping("/{user_id}")
    public ResponseEntity<WishListDto> referWishlist(
            @PathVariable(name = "user_id") Long userId
    ) {
        return wishItemService.referWishList(userId);
    }

    /**
    클라이언트가 위시리스트에서 담은 상품들을 주문하기 위해 order-service로 통신하는 API
    */
    @PostMapping("/{user_id}")
    public ResponseEntity<ResponseDto> purchaseWishList(
            @PathVariable(name = "user_id") Long userId,
            @RequestBody AddressDto addressDto,
            @RequestBody List<WishItemDto> dtos // 서
    ) {

        return wishItemService.purchaseWishList(userId, addressDto, dtos);
    }

    /**
     사용자의 주소 중 한 곳으로 위시리스트 상품 모두를 주문하는 API
     */
    @PostMapping("/{user_id}/test")
    public ResponseEntity<ResponseDto> purchaseWishListTest(
            @PathVariable(name = "user_id") Long userId
    ) {
        return wishItemService.purchaseWishListTest(userId);
    }

    @PatchMapping("/{wish_item_id}/increase-count")
    public ResponseEntity<ResponseDto> increaseCount(
            @PathVariable(name = "wish_item_id") Long wishItemId
    ) {
        return wishItemService.increaseCount(wishItemId);
    }

    @PatchMapping("/{wish_item_id}/decrease-count")
    public ResponseEntity<ResponseDto> decreaseCount(
            @PathVariable(name = "wish_item_id") Long wishItemId
    ) {
        return wishItemService.decreaseCount(wishItemId);
    }

    @DeleteMapping("/{wish_item_id}/")
    public ResponseEntity<ResponseDto> delete(
            @PathVariable(name = "wish_item_id") Long wishItemId
    ) {
        return wishItemService.decreaseCount(wishItemId);
    }
}
