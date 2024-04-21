package com.layby.domain.dto.response;

import com.layby.domain.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
@AllArgsConstructor
public class ItemResponseDto {

    private String itemName;

    private int price;

    private String details;

    public ItemResponseDto(Item item) {
        this.itemName = item.getItemName();
        this.price = item.getPrice();
        this.details = item.getDetails();
    }
}
