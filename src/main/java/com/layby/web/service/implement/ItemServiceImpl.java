package com.layby.web.service.implement;

import com.layby.domain.dto.response.ItemListResponseDto;
import com.layby.domain.dto.response.ItemResponseDto;
import com.layby.domain.entity.ItemEntity;
import com.layby.domain.repository.ItemRepository;
import com.layby.web.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

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
        ItemEntity itemEntity = itemRepository.findById(itemId).orElse(null);
        return ResponseEntity.status(HttpStatus.OK).body(new ItemResponseDto(itemEntity));
    }
}
