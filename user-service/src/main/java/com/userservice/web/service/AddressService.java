package com.userservice.web.service;

import com.userservice.domain.dto.AddressListDto;
import com.userservice.domain.dto.ResponseDto;
import com.userservice.domain.entity.Address;
import com.userservice.domain.entity.User;
import com.userservice.domain.vo.AddressRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AddressService {

    Address findByAddressId(Long addressId);

    List<Address> findAllByUserId(Long userId);

    ResponseEntity<ResponseDto> updateAddress(Long addressId, AddressRequest dto);

    ResponseEntity<ResponseDto> addAddress(Long userId, AddressRequest dto);

    ResponseEntity<AddressListDto> referAddressListByUserId(Long userId);
}
