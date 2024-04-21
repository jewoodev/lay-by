package com.layby.domain.dto.response;

import com.layby.domain.entity.ItemEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
@AllArgsConstructor
public class ItemResponseDto {

    private String itemName;

    private int price;

    private String details;

    public ItemResponseDto(ItemEntity itemEntity) {
        this.itemName = itemEntity.getItemName();
        this.price = itemEntity.getPrice();
        this.details = itemEntity.getDetails();
    }
}
