package com.layby.web.service.implement;

import com.layby.domain.dto.request.ItemSaveRequestDto;
import com.layby.domain.dto.response.ItemListResponseDto;
import com.layby.domain.dto.response.ItemResponseDto;
import com.layby.domain.dto.response.ItemSaveResponseDto;
import com.layby.domain.entity.Item;
import com.layby.domain.repository.ItemRepository;
import com.layby.web.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public Item findByItemId(Long itemId) {
        return itemRepository.findById(itemId).orElse(null);
    }

    @Override
    @Transactional
    public ResponseEntity<? super ItemSaveResponseDto> saveItem(ItemSaveRequestDto dto) {
        Item item = new Item(dto);
        itemRepository.save(item);
        return ItemSaveResponseDto.success();
    }

    @Override
    public ResponseEntity<List<ItemListResponseDto>> referItemList() {
        List<ItemListResponseDto> itemList = itemRepository.findAll().stream()
                .map(itemEntity -> {
                    return ItemListResponseDto.builder()
                            .itemName(itemEntity.getItemName())
                            .price(itemEntity.getPrice())
                            .build();
                })
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(itemList);
    }

    @Override
    public ResponseEntity<ItemResponseDto> referItem(Long itemId) {
        Item item = itemRepository.findById(itemId).orElse(null);
        return ResponseEntity.status(HttpStatus.OK).body(new ItemResponseDto(item));
    }
}
