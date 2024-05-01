package com.itemservice.domain.vo.response;

import com.itemservice.domain.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemStockResponse {

    private int stockQuantity;

    public static ItemStockResponse fromItem(Item item) {
        return new ItemStockResponse(item.getStockQuantity());
    }
}
