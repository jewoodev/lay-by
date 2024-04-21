package com.layby.web.controller;

import com.layby.domain.dto.request.PhoneNumberUpdateRequestDto;
import com.layby.domain.dto.request.UserPasswordUpdateRequestDto;
import com.layby.domain.dto.response.PhoneNumberUpdateResponseDto;
import com.layby.domain.dto.response.UserPasswordUpdateResponseDto;
import com.layby.domain.dto.response.UserResponseDto;
import com.layby.web.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{user_id}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public UserResponseDto referUser(@PathVariable(name = "user_id") Long userId) {
        return userService.referUser(userId);
    }

    @PatchMapping("/{user_id}/update-phone-number")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<? super PhoneNumberUpdateResponseDto> updatePhoneNumber(
            @PathVariable(name = "user_id") Long userId,
            @RequestBody @Valid PhoneNumberUpdateRequestDto dto
    ) {
        return userService.updatePhoneNumber(userId, dto);
    }

    @PatchMapping("/{user_id}/update-password")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<? super UserPasswordUpdateResponseDto> updatePassword(
            @PathVariable(name = "user_id") Long userId,
            @RequestBody @Valid UserPasswordUpdateRequestDto dto
    ) {
        return userService.updatePassword(userId, dto);
    }
}
