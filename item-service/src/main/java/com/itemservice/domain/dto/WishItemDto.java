package com.itemservice.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itemservice.domain.entity.WishItem;
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

    //== 생성자 메서드 ==//
    /** 위시아이템 엔티티로 WishItemDto를 만드는 메서드 **/
    public static WishItemDto fromWishItem(WishItem wishItem) {

        return WishItemDto.builder()
                .wishItemId(wishItem.getWishItemId())
                .itemId(wishItem.getItemId())
                .itemName(wishItem.getItemName())
                .price(wishItem.getPrice())
                .count(wishItem.getCount())
                .totalPrice(wishItem.getTotalPrice())
                .build();
    }
}
