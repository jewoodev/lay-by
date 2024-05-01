package com.itemservice.web.controller;

import com.itemservice.domain.dto.*;
import com.itemservice.domain.vo.request.ItemRequest;
import com.itemservice.domain.vo.request.ItemStockControlRequests;
import com.itemservice.domain.vo.request.WishItemRequest;
import com.itemservice.domain.vo.response.ItemStockResponse;
import com.itemservice.web.service.ItemService;
import com.itemservice.web.service.WishItemService;
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

    @GetMapping("/{item_id}/stock")
    public ResponseEntity<ItemStockResponse> referItemsStock(
            @PathVariable(name = "item_id") Long itemId
    ) {
        return itemService.referStock(itemId);
    }

    @PostMapping("/")
    public ResponseEntity<ResponseDto> create(@RequestBody ItemRequest request) {
        return itemService.saveItem(request);
    }

    @PostMapping("/{item_id}/{user_id}")
    public ResponseEntity<ResponseDto> registerToWishlist(
            @PathVariable(name = "item_id") Long itemId,
            @PathVariable(name = "user_id") Long userId,
            @RequestBody WishItemRequest request
    ) {
        return wishItemService.register(itemId, userId, request);
    }

    @PutMapping("/ic")
    public ResponseEntity<ResponseDto> increaseStock(@RequestBody ItemStockControlRequests itemStockControlRequests) {
        return itemService.increaseStock(itemStockControlRequests);
    }

//    @PutMapping("/dc")
//    public ResponseEntity<ResponseDto> decreaseStock(@RequestBody List<ItemStockDto> requests) {
//        return itemService.decreaseStock(requests);
//    }
}
