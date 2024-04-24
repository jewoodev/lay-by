package com.layby.domain.entity;

import com.layby.domain.dto.request.AddressRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "address")
@Entity(name = "address")
public class Address extends BaseTimeEntity {

    @Id
    @Column(name = "address_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "zipcode")
    private String zipCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void updateAddress(AddressRequestDto dto) {
        this.city = dto.getCity();
        this.street = dto.getStreet();
        this.zipCode = dto.getZipCode();
    }
}
