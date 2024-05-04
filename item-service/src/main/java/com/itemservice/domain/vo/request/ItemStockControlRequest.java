package com.itemservice.domain.vo.request;

import com.itemservice.domain.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemStockControlRequest {

    private Long itemId;

    private int count;
}
