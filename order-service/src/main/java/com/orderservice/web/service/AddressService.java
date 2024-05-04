package com.orderservice.web.service;

import com.orderservice.domain.dto.AddressListDto;
import com.orderservice.domain.dto.ResponseDto;
import com.orderservice.domain.entity.Address;
import com.orderservice.domain.vo.AddressRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AddressService {

    Address findByAddressId(Long addressId);

    List<Address> findAllByUserId(Long userId);

    ResponseEntity<ResponseDto> updateAddress(Long addressId, AddressRequest dto);

    ResponseEntity<ResponseDto> addAddress(Long userId, AddressRequest dto);

    ResponseEntity<AddressListDto> referAddressListByUserId(Long userId);
}
