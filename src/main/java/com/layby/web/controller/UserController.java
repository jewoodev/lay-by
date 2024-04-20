package com.layby.web.controller;

import com.layby.domain.dto.request.PhoneNumberUpdateRequestDto;
import com.layby.domain.dto.request.UserPasswordUpdateRequestDto;
import com.layby.domain.dto.response.ResponseDto;
import com.layby.domain.dto.response.UserResponseDto;
import com.layby.domain.entity.UserEntity;
import com.layby.domain.repository.UserRepository;
import com.layby.web.exception.AES256Exception;
import com.layby.web.service.UserService;
import com.layby.web.util.AES256;
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
    private final AES256 personalDataEncoder;

    @GetMapping("/{user_id}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public UserResponseDto referUser(@PathVariable(name = "user_id") Long userId) {
        return userService.referUser(userId);
    }

    @PatchMapping("/{user_id}/update-phone-number")
    public ResponseEntity<ResponseDto> updatePhoneNumber(
            @PathVariable(name = "user_id") Long userId,
            @RequestBody @Valid PhoneNumberUpdateRequestDto dto
    ) {
        return userService.updatePhoneNumber(userId, dto);
    }

    @PatchMapping("/{user_id}/update-password")
    public ResponseEntity<ResponseDto> updatePassword(
            @PathVariable(name = "user_id") Long userId,
            @RequestBody @Valid UserPasswordUpdateRequestDto dto
    ) {
        return userService.updatePassword(userId, dto);
    }
}
