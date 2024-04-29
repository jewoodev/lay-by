package com.itemservice.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itemservice.domain.entity.Address;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddressDto {

    @JsonIgnore
    private Long addressId;

    private String city;

    private String street;

    private String zipCode;


    //== 생성자 ==//
    // Address -> AddressReferResponseDto로 변환하는 생성자
    public AddressDto(Address address) {
        this.addressId = address.getAddressId();
        this.city = address.getCity();
        this.street = address.getStreet();
        this.zipCode = address.getZipCode();
    }
}
