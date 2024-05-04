package com.orderservice.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.orderservice.domain.entity.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class AddressDto {

    @JsonIgnore
    private Long addressId;

    private String city;

    private String street;

    private String zipCode;


    //== 생성 메서드 ==//
    // Address -> AddressReferResponseDto로 변환하는 생성자
    public static AddressDto fromAddress(Address address) {
        return AddressDto.builder()
                .addressId(address.getAddressId())
                .city(address.getCity())
                .street(address.getStreet())
                .zipCode(address.getZipCode())
                .build();
    }
}
