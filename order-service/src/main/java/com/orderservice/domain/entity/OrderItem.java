package com.orderservice.domain.entity;

import com.orderservice.domain.dto.WishItemDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_item")
@Entity(name = "order_item")
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long orderItemId;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "price")
    private int price;

    @Column(name = "count")
    private int count;

    @Column(name = "total_price")
    private int totalPrice;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "order_id")
    private Long orderId;

    public void mappingOrder(Long orderId) {
        this.orderId = orderId;
    }

    //== 생성 메서드 ==//
    /** item-service로 통신받은 위시리스트 상품을 orderItem으로 변환, order_id는 주문 생성 후에 mappingOrder로 매핑한다 **/
    public static OrderItem fromWishItemDto(
            WishItemDto dto
    ) {

        OrderItem orderItem = OrderItem.builder()
                .itemName(dto.getItemName())
                .price(dto.getPrice())
                .count(dto.getCount())
                .totalPrice(dto.getTotalPrice())
                .itemId(dto.getItemId())
                .build();

        return orderItem;
    }
}
