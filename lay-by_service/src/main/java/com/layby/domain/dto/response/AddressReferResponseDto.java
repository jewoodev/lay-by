package com.layby.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.layby.domain.entity.Address;
import com.layby.domain.entity.WishItem;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class AddressReferResponseDto {

    @JsonIgnore
    private Long addressId;

    private String city;

    private String street;

    private String zipCode;


    //== 생성자 ==//
    // Address -> AddressReferResponseDto로 변환하는 생성자
    public AddressReferResponseDto(Address address) {
        this.addressId = address.getAddressId();
        this.city = address.getCity();
        this.street = address.getStreet();
        this.zipCode = address.getZipCode();
    }
}
