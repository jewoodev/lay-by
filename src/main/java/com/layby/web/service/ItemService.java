package com.layby.web.service;

import com.layby.domain.dto.request.ItemSaveRequestDto;
import com.layby.domain.dto.response.ItemListResponseDto;
import com.layby.domain.dto.response.ItemResponseDto;
import com.layby.domain.dto.response.ResponseDto;
import com.layby.domain.entity.Item;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ItemService {

    Item findByItemId(Long itemId);

    Item findByItemName(String itemName);

    ResponseEntity<ResponseDto> saveItem(ItemSaveRequestDto dto);

    ResponseEntity<List<ItemListResponseDto>> referItemList();

    ResponseEntity<ItemResponseDto> referItem(Long itemId);
}
