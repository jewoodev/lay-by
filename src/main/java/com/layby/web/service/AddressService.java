package com.layby.web.service;

import com.layby.domain.dto.request.AddressRequestDto;
import com.layby.domain.dto.response.AddressUpdateResponseDto;
import com.layby.domain.entity.Address;
import org.springframework.http.ResponseEntity;

public interface AddressService {

    Address findByAddressId(Long addressId);


    ResponseEntity<? super AddressUpdateResponseDto> updateAddress(Long addressId, AddressRequestDto dto);

    ResponseEntity<? super AddressUpdateResponseDto> addAddress(String username, AddressRequestDto dto);
}
