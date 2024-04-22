package com.layby.domain.dto.response;

import com.layby.domain.entity.WishItem;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class WishListReferResponseDto {

    private List<WishItemResponseDto> wishItemResponseDtos = new ArrayList<>();

    private int totalPrice = 0;

    public WishListReferResponseDto(List<WishItem> wishItemEntities) {

        for (WishItem wishItem : wishItemEntities) {
            this.wishItemResponseDtos
                    .add(
              WishItemResponseDto.fromWishItem(wishItem)
            );

            totalPrice += wishItem.getTotalPrice();
        }
    }
}
