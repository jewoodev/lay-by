package com.layby.domain.entity;

import com.layby.domain.common.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

@Setter @Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "delivery")
@Entity(name = "delivery")
public class DeliveryEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long deliveryId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private AddressEntity addressEntity;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @OneToOne(mappedBy = "deliveryEntity", fetch = FetchType.LAZY)
    private OrderEntity orderEntity;
}
