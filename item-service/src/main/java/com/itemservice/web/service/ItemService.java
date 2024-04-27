package com.itemservice.web.service;

import com.itemservice.domain.dto.ItemDto;
import com.itemservice.domain.dto.ItemListDto;
import com.itemservice.domain.dto.ResponseDto;
import com.itemservice.domain.entity.Item;
import com.itemservice.domain.vo.ItemRequest;
import com.itemservice.domain.vo.ItemStockAddRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ItemService {

    Item findByItemId(Long itemId);

    Item findByItemName(String itemName);

    ResponseEntity<ResponseDto> saveItem(ItemRequest vo);

    ResponseEntity<List<ItemListDto>> referItemList();

    ResponseEntity<ItemDto> referItem(Long itemId);

    ResponseEntity<ResponseDto> increaseStock(List<ItemStockAddRequest> requests);
}
