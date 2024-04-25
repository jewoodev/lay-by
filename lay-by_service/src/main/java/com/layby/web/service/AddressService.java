package com.layby.web.service;

import com.layby.domain.dto.request.AddressRequestDto;
import com.layby.domain.dto.response.AddressListReferResponseDto;
import com.layby.domain.dto.response.ResponseDto;
import com.layby.domain.entity.Address;
import com.layby.domain.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AddressService {

    Address findByAddressId(Long addressId);

    List<Address> findAllByUser(User user);

    ResponseEntity<ResponseDto> updateAddress(Long addressId, AddressRequestDto dto);

    ResponseEntity<ResponseDto> addAddress(String username, AddressRequestDto dto);

    ResponseEntity<AddressListReferResponseDto> referAddressListByUserId(Long userId);
}
