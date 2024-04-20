package com.layby.web.controller;

import com.layby.domain.dto.request.AddressRequestDto;
import com.layby.domain.dto.response.ResponseDto;
import com.layby.domain.entity.AddressEntity;
import com.layby.web.filter.JwtAuthenticationFilter;
import com.layby.web.jwt.JwtProvider;
import com.layby.web.service.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/address")
public class AddressController {

    private final AddressService addressService;
    private final JwtProvider jwtProvider;

    @PutMapping("/{address_id}/update")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<ResponseDto> updateAddress(@PathVariable(name = "address_id") Long addressId,
                                                     AddressRequestDto dto) {
        return addressService.updateAddress(addressId, dto);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<ResponseDto> addAddress(Authentication authentication, AddressRequestDto dto) {
        String username = authentication.getPrincipal().toString();
        return addressService.addAddress(username, dto);
    }
}
