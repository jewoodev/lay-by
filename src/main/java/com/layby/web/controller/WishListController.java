package com.layby.web.controller;

import com.layby.domain.dto.response.WishListReferResponseDto;
import com.layby.web.service.WishListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/wishlist")
public class WishListController {

    private final WishListService wishlistService;

    @GetMapping("/{user_id}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<WishListReferResponseDto> referWishlist(
            @PathVariable(name = "user_id") Long userId
    ) {
        return wishlistService.referWishlist(userId);
    }
}
