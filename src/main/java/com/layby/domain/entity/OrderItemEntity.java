package com.layby.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter @Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_item")
@Entity(name = "order_item")
public class OrderItemEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long orderItemId;

    @Column(name = "order_price")
    private int orderPrice;

    @Column(name = "count")
    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemEntity itemEntity;
}
