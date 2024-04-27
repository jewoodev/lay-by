package com.orderservice.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddressDto {

    @JsonIgnore
    private Long addressId;

    private String city;

    private String street;

    private String zipCode;
}
