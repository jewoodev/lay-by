package com.itemservice.web.service.implement;

import com.itemservice.domain.dto.*;
import com.itemservice.domain.entity.Item;
import com.itemservice.domain.repository.ItemRepository;
import com.itemservice.domain.repository.RedissonLockItemFacade;
import com.itemservice.domain.vo.request.ItemStockControlRequest;
import com.itemservice.domain.vo.request.ItemStockControlRequests;
import com.itemservice.domain.vo.response.ItemStockResponse;
import com.itemservice.web.service.ItemService;
import com.itemservice.domain.vo.request.ItemRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;


@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public Item findByItemId(Long itemId) {
        return itemRepository.findByItemId(itemId);
    }

    @Override
    public Item findByItemName(String itemName) {
        return itemRepository.findByItemName(itemName);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> saveItem(ItemRequest vo) {
        Item item = Item.fromRequest(vo);
        itemRepository.save(item);

        return ResponseDto.success();
    }

    @Override
    public ResponseEntity<List<ItemListDto>> referItemList() {
        List<ItemListDto> itemList = itemRepository.findAll().stream()
                .map(itemEntity -> {
                    return ItemListDto.builder()
                            .itemName(itemEntity.getItemName())
                            .price(itemEntity.getPrice())
                            .build();
                })
                .collect(Collectors.toList());

        return ResponseEntity.status(OK).body(itemList);
    }

    @Override
    public ResponseEntity<ItemDto> referItem(Long itemId) {
        Item item = itemRepository.findById(itemId).orElse(null);
        return ResponseEntity.status(OK).body(ItemDto.fromItem(item));
    }

    @Override
    public ResponseEntity<ItemStockResponse> referStock(Long itemId) {
        Item item = itemRepository.findByItemId(itemId);
        ItemStockResponse itemStockResponse = ItemStockResponse.fromItem(item);
        return ResponseEntity.status(OK).body(itemStockResponse);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> increaseStock(ItemStockControlRequests itemStockControlRequests) {
        for (ItemStockControlRequest request : itemStockControlRequests.getItemStockControlRequests()) {
            Item item = itemRepository.findByItemIdWithPMCLock(request.getItemId());
            item.addStock(request.getCount());
        }
        return ResponseDto.success();
    }

    @Override
    @Transactional
    public void decreaseStock(ItemStockControlRequests requests) {
        for (ItemStockControlRequest itemStockControlRequest : requests.getItemStockControlRequests()) {
            Item item = itemRepository.findByItemIdWithPMCLock(itemStockControlRequest.getItemId());
            item.removeStock(itemStockControlRequest.getCount());
        }
    }
}
