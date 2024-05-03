package com.orderservice.web.controller;

import com.orderservice.domain.dto.AddressListDto;
import com.orderservice.domain.dto.ResponseDto;
import com.orderservice.domain.vo.AddressRequest;
import com.orderservice.web.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/{user_id}")
    public ResponseEntity<AddressListDto> referAddressList(
            @PathVariable(name = "user_id") Long userId
    ) {
        return addressService.referAddressListByUserId(userId);
    }

    @PutMapping("/{address_id}/update")
    public ResponseEntity<ResponseDto> updateAddress(
            @PathVariable(name = "address_id") Long addressId,
            @RequestBody @Valid AddressRequest request
    ) {
        return addressService.updateAddress(addressId, request);
    }

    @PostMapping("/{user_id}/add")
    public ResponseEntity<ResponseDto> addAddress(
            @PathVariable(name = "user_id") Long userId,
            @RequestBody @Valid AddressRequest request
    ) {
        return addressService.addAddress(userId, request);
    }
}
