package com.itemservice.domain.vo.request;

import com.itemservice.domain.entity.WishItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemStockControlRequests {

    List<ItemStockControlRequest> itemStockControlRequests;

    //== 생성 메서드 ==//
    /** 위시리스트에 담긴 상품들로 수량 컨트롤 데이터 객체를 만드는 메서드 **/
    public static ItemStockControlRequests fromWishItems(List<WishItem> wishItems) {

        List<ItemStockControlRequest> requests = new ArrayList<>();

        for (WishItem wishItem : wishItems) {
            requests.add(new ItemStockControlRequest(wishItem.getItemId(), wishItem.getCount()));
        }

        return new ItemStockControlRequests(requests);
    }
}
