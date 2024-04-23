package com.layby.domain.entity;

import com.layby.domain.common.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "delivery")
@Entity(name = "delivery")
public class Delivery extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long deliveryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    public void mappingOrder(Order order) {
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

    /** 날짜를 체크해 배송 상태를 업데이트하는 메서드 **/
    public DeliveryStatus checkStatus() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createdDate = getCreatedDate();

        long passDays = ChronoUnit.DAYS.between(now, createdDate);
        if (passDays == 1L) updateStatus(DeliveryStatus.PROCESS);
        else if (passDays > 1L) updateStatus(DeliveryStatus.COMPLETE);

        return this.deliveryStatus;
    }

    /** 배송 정보가 생성되고 지난 일 수를 반환하는 메서드 **/
    public long checkPastDay() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createdDate = getCreatedDate();

        long passDays = ChronoUnit.DAYS.between(createdDate, now);
        return passDays;
    }
}
