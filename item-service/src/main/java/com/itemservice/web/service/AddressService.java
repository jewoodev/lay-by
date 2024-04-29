package com.itemservice.web.service;

import com.itemservice.domain.dto.AddressListDto;
import com.itemservice.domain.dto.ResponseDto;
import com.itemservice.domain.entity.Address;
import com.itemservice.domain.vo.AddressRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AddressService {

    Address findByAddressId(Long addressId);

    List<Address> findAllByUserId(Long userId);

    ResponseEntity<ResponseDto> updateAddress(Long addressId, AddressRequest dto);

    ResponseEntity<ResponseDto> addAddress(Long userId, AddressRequest dto);

    ResponseEntity<AddressListDto> referAddressListByUserId(Long userId);
}
