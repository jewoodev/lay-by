package com.itemservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemStockDtoList {

    List<ItemStockDto> itemStockDtos;

    public static ItemStockDtoList fromWishItemDtos(List<WishItemDto> wishItemDtos) {
        List<ItemStockDto> itemStockDtos = new ArrayList<>();

        for (WishItemDto wishItemDto : wishItemDtos) {
            ItemStockDto itemStockDto = new ItemStockDto(wishItemDto.getItemId(), wishItemDto.getCount());
            itemStockDtos.add(itemStockDto);
        }

        return new ItemStockDtoList(itemStockDtos);
    }
}
