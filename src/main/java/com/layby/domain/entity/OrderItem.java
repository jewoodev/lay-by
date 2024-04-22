package com.layby.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_item")
@Entity(name = "order_item")
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long orderItemId;

    @Column(name = "order_price")
    private int orderPrice;

    @Column(name = "count")
    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public void mappingOrder(Order order) {
        this.order = order;
    }

    //== 생성 메서드 ==//
    public static OrderItem convertFromWishItem(
            WishItem wishItem
    ) {
        Item item = wishItem.getItem();
        int count = wishItem.getCount();

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .orderPrice(wishItem.getPrice())
                .count(count)
                .build();

        item.removeStock(count);
        return orderItem;
    }

    //== 비즈니스 로직 ==//
    /** 주문 취소 **/
    public void cancel() {
        this.item.addStock(this.count);
    }

    //== 조회 로직 ==//
    /** 주문 상품 전체 가격 조회 **/
    public int getTotalPrice() {
        return this.orderPrice * this.count;
    }
}
