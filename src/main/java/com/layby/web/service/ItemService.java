package com.layby.web.service;

import com.layby.domain.dto.request.ItemSaveRequestDto;
import com.layby.domain.dto.response.ItemListResponseDto;
import com.layby.domain.dto.response.ItemResponseDto;
import com.layby.domain.dto.response.ItemSaveResponseDto;
import com.layby.domain.entity.Item;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ItemService {

    Item findByItemId(Long itemId);

    ResponseEntity<? super ItemSaveResponseDto> saveItem(ItemSaveRequestDto dto);

    ResponseEntity<List<ItemListResponseDto>> referItemList();

    ResponseEntity<ItemResponseDto> referItem(Long itemId);


}
