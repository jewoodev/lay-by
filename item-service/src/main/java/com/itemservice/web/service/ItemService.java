package com.itemservice.web.service;

import com.itemservice.domain.dto.*;
import com.itemservice.domain.entity.Item;
import com.itemservice.domain.vo.request.ItemRequest;
import com.itemservice.domain.vo.request.ItemStockControlRequests;
import com.itemservice.domain.vo.response.ItemStockResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ItemService {

    Item findByItemId(Long itemId);

    Item findByItemName(String itemName);

    ResponseEntity<ResponseDto> saveItem(ItemRequest vo);

    ResponseEntity<List<ItemListDto>> referItemList();

    ResponseEntity<ItemDto> referItem(Long itemId);

    ResponseEntity<ItemStockResponse> referStock(Long itemId);

    ResponseEntity<ResponseDto> increaseStock(ItemStockControlRequests requests);

    void decreaseStock(ItemStockControlRequests requests);
}
