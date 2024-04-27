package com.orderservice.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class WishItemDto {

    @JsonIgnore
    private Long wishItemId;

    private String itemName;

    private int price;

    private int count;

    private int totalPrice;

    @JsonIgnore
    private Long itemId;

    @JsonIgnore
    private Long userId;
}
