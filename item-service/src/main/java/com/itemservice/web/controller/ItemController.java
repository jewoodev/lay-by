package com.itemservice.web.controller;

import com.itemservice.domain.dto.ItemDto;
import com.itemservice.domain.dto.ItemListDto;
import com.itemservice.domain.dto.ResponseDto;
import com.itemservice.web.service.ItemService;
import com.itemservice.web.service.WishItemService;
import com.itemservice.domain.vo.ItemStockAddRequest;
import com.itemservice.domain.vo.WishItemRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;
    private final WishItemService wishItemService;

    @GetMapping("/all")
    public ResponseEntity<List<ItemListDto>> referItemList() {
        return itemService.referItemList();
    }

    @GetMapping("/{item_id}")
    public ResponseEntity<ItemDto> referItem(
            @PathVariable(name = "item_id") Long itemId
    ) {
        return itemService.referItem(itemId);
    }

    @PostMapping("/{item_id}/{user_id}")
    public ResponseEntity<ResponseDto> registerToWishlist(
            @PathVariable(name = "item_id") Long itemId,
            @PathVariable(name = "user_id") Long userId,
            @RequestBody @Valid WishItemRequest request
    ) {
        return wishItemService.register(itemId, userId, request);
    }

    @PatchMapping("/{item_id}")
    public ResponseEntity<ResponseDto> increaseStock(List<ItemStockAddRequest> requests) {
        return itemService.increaseStock(requests);
    }
}
