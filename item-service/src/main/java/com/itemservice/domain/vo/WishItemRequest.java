package com.itemservice.domain.vo;

import lombok.Getter;

@Getter
public class WishItemRequest {

    private Long itemId;

    private String itemName;

    private int price;

    private int count;
}
