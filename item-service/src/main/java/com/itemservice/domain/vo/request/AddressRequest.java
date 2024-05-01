package com.itemservice.domain.vo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AddressRequest {

    @NotBlank
    private String city;

    @NotBlank
    private String street;

    @NotBlank
    private String zipCode;
}
