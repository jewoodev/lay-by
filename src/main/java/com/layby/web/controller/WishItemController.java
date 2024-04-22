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
import org.springframework.security.core.parameters.P;
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
}
