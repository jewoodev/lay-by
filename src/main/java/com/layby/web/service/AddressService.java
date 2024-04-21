package com.layby.web.service;

import com.layby.domain.dto.request.AddressRequestDto;
import com.layby.domain.dto.response.AddressUpdateResponseDto;
import com.layby.domain.dto.response.ResponseDto;
import com.layby.domain.entity.AddressEntity;
import org.springframework.http.ResponseEntity;

public interface AddressService {

    AddressEntity findByAddressId(Long addressId);


    ResponseEntity<? super AddressUpdateResponseDto> updateAddress(Long addressId, AddressRequestDto dto);

    ResponseEntity<? super AddressUpdateResponseDto> addAddress(String username, AddressRequestDto dto);
}