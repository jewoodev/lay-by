package com.itemservice.web.controller;

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

    /**
     위시리스트에 담은 상품들을 조회하는 API
     */
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
    public ResponseEntity<ResponseDto> chooseForPurchase(
            @PathVariable(name = "user_id") Long userId,
            @RequestBody List<WishItemDto> dtos
    ) {

        return wishItemService.chooseForPurchase(userId, dtos);
    }

    /**
     사용자의 주소 중 한 곳으로 위시리스트 상품 모두를 주문하는 API
     */
    @PostMapping("/{user_id}/test")
    public ResponseEntity<ResponseDto> chooseForPurchaseTest(
            @PathVariable(name = "user_id") Long userId
    ) {
        return wishItemService.chooseForPurchaseTest(userId);
    }

    /**
     선택한 상품 갯수를 늘리는 API
     */
    @PatchMapping("/{wish_item_id}/increase-count")
    public ResponseEntity<ResponseDto> increaseCount(
            @PathVariable(name = "wish_item_id") Long wishItemId
    ) {
        return wishItemService.increaseCount(wishItemId);
    }

    /**
     선택한 상품 갯수를 줄이는 API
     */
    @PatchMapping("/{wish_item_id}/decrease-count")
    public ResponseEntity<ResponseDto> decreaseCount(
            @PathVariable(name = "wish_item_id") Long wishItemId
    ) {
        return wishItemService.decreaseCount(wishItemId);
    }

    /**
     위시리스트에 담은 상품을 위시리스트에서 제거하는 API
     */
    @DeleteMapping("/{wish_item_id}/")
    public ResponseEntity<ResponseDto> delete(
            @PathVariable(name = "wish_item_id") Long wishItemId
    ) {
        return wishItemService.decreaseCount(wishItemId);
    }
}
