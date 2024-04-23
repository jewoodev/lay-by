package com.layby.web.controller;

import com.layby.domain.dto.response.AddressReferResponseDto;
import com.layby.domain.dto.response.ResponseDto;
import com.layby.domain.dto.response.WishItemResponseDto;
import com.layby.domain.dto.response.WishListReferResponseDto;
import com.layby.web.service.WishItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/wish-item/")
public class WishItemController {

    private final WishItemService wishItemService;

    @GetMapping("/{user_id}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<WishListReferResponseDto> referWishlist(
            @PathVariable(name = "user_id") Long userId
    ) {
        return wishItemService.referWishList(userId);
    }

    /**
    클라이언트가 위시리스트에서 구입할 상품을 고르고 배송받을 주소를 고를 때 사용되는 API
    */
    @PostMapping("/{user_id}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<ResponseDto> purchaseWishList(
            @PathVariable(name = "user_id") Long userId,
            @RequestBody AddressReferResponseDto addressReferResponseDto,
            @RequestBody List<WishItemResponseDto> dtos
    ) {
        Long addressId = addressReferResponseDto.getAddressId();
        return wishItemService.purchaseWishList(userId, addressId, dtos);
    }

    /**
     사용자의 주소 중 한 곳으로 위시리스트 상품 모두를 주문하는 API
     */
    @PostMapping("/{user_id}/test")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<ResponseDto> purchaseWishListTest(
            @PathVariable(name = "user_id") Long userId
    ) {
        return wishItemService.purchaseWishListTest(userId);
    }

    @PatchMapping("/{wish_item_id}/increase-count")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<ResponseDto> increaseCount(
            @PathVariable(name = "wish_item_id") Long wishItemId
    ) {
        return wishItemService.increaseCount(wishItemId);
    }

    @PatchMapping("/{wish_item_id}/decrease-count")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<ResponseDto> decreaseCount(
            @PathVariable(name = "wish_item_id") Long wishItemId
    ) {
        return wishItemService.decreaseCount(wishItemId);
    }

    @DeleteMapping("/{wish_item_id}/")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<ResponseDto> delete(
            @PathVariable(name = "wish_item_id") Long wishItemId
    ) {
        return wishItemService.decreaseCount(wishItemId);
    }
}
