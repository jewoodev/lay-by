package com.layby.web.controller;

import com.layby.domain.dto.request.WishItemSaveRequestDto;
import com.layby.domain.dto.response.ItemListResponseDto;
import com.layby.domain.dto.response.ItemResponseDto;
import com.layby.domain.dto.response.ResponseDto;
import com.layby.web.service.ItemService;
import com.layby.web.service.WishItemService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/v1/item")
public class ItemController {

    private final ItemService itemService;
    private final WishItemService wishItemService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<List<ItemListResponseDto>> referItemList() {
        return itemService.referItemList();
    }

    @GetMapping("/{item_id}")
    public ResponseEntity<ItemResponseDto> referItem(
            @PathVariable(name = "item_id") Long itemId
    ) {
        return itemService.referItem(itemId);
    }

    @PostMapping("/{item_id}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<ResponseDto> registerToWishlist(
            @PathVariable(name = "item_id") Long itemId,
            Authentication authentication,
            @RequestBody @Valid WishItemSaveRequestDto dto
    ) {
        return wishItemService.register(itemId, authentication, dto);
    }
}
