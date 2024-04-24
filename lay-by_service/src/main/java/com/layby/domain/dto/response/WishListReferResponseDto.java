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

    //== 생성자 ==//
    /** 위시아이템 엔티티 리스트로 WishListReferResponseDto를 만드는 생성자 **/
    public WishListReferResponseDto(List<WishItem> wishItems) {

        for (WishItem wishItem : wishItems) {
            this.wishItemResponseDtos
                    .add(
              WishItemResponseDto.fromWishItem(wishItem)
            );

            totalPrice += wishItem.getTotalPrice();
        }
    }
}
