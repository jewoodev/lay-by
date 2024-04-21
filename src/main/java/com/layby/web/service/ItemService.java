package com.layby.web.service;

import com.layby.domain.dto.response.ItemListResponseDto;
import com.layby.domain.dto.response.ItemResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ItemService {

    ResponseEntity<List<ItemListResponseDto>> referItemList();

    ResponseEntity<ItemResponseDto> referItem(Long itemId);
}
