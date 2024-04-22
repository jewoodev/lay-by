package com.layby.domain.entity;

import com.layby.domain.common.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;
import org.aspectj.weaver.ast.Or;

@Getter
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

    // 주문 창에서 필요한 생성자
    @Builder
    public Delivery(Address address) {
        this.address = address;
        this.deliveryStatus = DeliveryStatus.PREPARE;
    }

    //== 비즈니스 로직 ==//

    /** 배송 상태 업데이트하는 메서드 **/
    public void updateStatus(DeliveryStatus status) {
        this.deliveryStatus = status;
    }
}
