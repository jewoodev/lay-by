package com.userservice.web.controller;

import com.userservice.domain.dto.AddressDto;
import com.userservice.domain.dto.AddressListDto;
import com.userservice.domain.dto.ResponseDto;
import com.userservice.domain.entity.User;
import com.userservice.domain.vo.AddressRequest;
import com.userservice.web.service.AddressService;
import com.userservice.web.service.UserService;
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
    private final UserService userService;

    @GetMapping("/{user_id}")
    public ResponseEntity<AddressListDto> referAddressList(
            @PathVariable(name = "user_id") Long userId
    ) {
        return addressService.referAddressListByUserId(userId);
    }

    @PutMapping("/{address_id}/update")
    public ResponseEntity<ResponseDto> updateAddress(
            @PathVariable(name = "address_id") Long addressId,
            @RequestBody @Valid AddressRequest vo
    ) {
        return addressService.updateAddress(addressId, vo);
    }

    @PostMapping("/add/{user_id}")
    public ResponseEntity<ResponseDto> addAddress(
            @PathVariable(name = "user_id") Long userId,
            @RequestBody @Valid AddressRequest vo
    ) {
        return addressService.addAddress(userId, vo);
    }
}
