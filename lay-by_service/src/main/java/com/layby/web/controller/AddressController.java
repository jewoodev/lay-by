package com.layby.web.controller;

import com.layby.domain.dto.request.AddressRequestDto;
import com.layby.domain.dto.response.AddressListReferResponseDto;
import com.layby.domain.dto.response.ResponseDto;
import com.layby.web.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/address")
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/{user_id}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<AddressListReferResponseDto> referAddressList(
            @PathVariable(name = "user_id") Long userId
    ) {
        return addressService.referAddressListByUserId(userId);
    }

    @PutMapping("/{address_id}/update")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<ResponseDto> updateAddress(
            @PathVariable(name = "address_id") Long addressId,
            @RequestBody @Valid AddressRequestDto dto
    ) {
        return addressService.updateAddress(addressId, dto);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<ResponseDto> addAddress(
            Authentication authentication,
            @RequestBody @Valid AddressRequestDto dto
    ) {
        String username = authentication.getPrincipal().toString();
        return addressService.addAddress(username, dto);
    }
}
