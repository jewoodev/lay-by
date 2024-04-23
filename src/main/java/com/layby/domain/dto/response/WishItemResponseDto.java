package com.layby.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.layby.domain.entity.WishItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class WishItemResponseDto {

    @JsonIgnore
    private Long wishItemId;

    @JsonIgnore
    private Long itemId;

    private String itemName;

    private int price;

    private int count;

    private int totalPrice;

    //== 생성자 메서드 ==//
    /** 위시아이템 엔티티로 WishItemResponseDto를 만드는 메서드 **/
    public static WishItemResponseDto fromWishItem(WishItem wishItem) {

        WishItemResponseDto wishItemResponseDto = WishItemResponseDto.builder()
                .wishItemId(wishItem.getWishItemId())
                .itemId(wishItem.getItem().getItemId())
                .itemName(wishItem.getItem().getItemName())
                .price(wishItem.getPrice())
                .count(wishItem.getCount())
                .totalPrice(wishItem.getTotalPrice())
                .build();

        return wishItemResponseDto;
    }
}
