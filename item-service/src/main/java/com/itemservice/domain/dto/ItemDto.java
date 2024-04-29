package com.itemservice.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itemservice.domain.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
@AllArgsConstructor
public class ItemDto {

    @JsonIgnore
    private Long itemId;

    private String itemName;

    private int price;

    private String details;

    private int stockQuantity;

    //== 생성자 ==//
    public static ItemDto fromItem(Item item) {
        return ItemDto.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .price(item.getPrice())
                .details(item.getDetails())
                .stockQuantity(item.getStockQuantity())
                .build();
    }
}
