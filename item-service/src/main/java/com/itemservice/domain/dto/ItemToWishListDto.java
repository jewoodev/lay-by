package com.itemservice.domain.dto;

import lombok.Data;

@Data
public class ItemToWishListDto {

    private Long itemId;

    private String itemName;

    private int price;
}
