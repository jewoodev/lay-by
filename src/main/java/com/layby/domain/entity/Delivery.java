package com.layby.domain.entity;

import com.layby.domain.common.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "delivery")
@Entity(name = "delivery")
public class Delivery {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long deliveryId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    public void mappingOrderEntity(Order order) {
        this.order = order;
    }
}
