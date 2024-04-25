package com.userservice.web.controller;

import com.userservice.domain.dto.ResponseDto;
import com.userservice.domain.dto.request.PhoneNumberUpdateRequestDto;
import com.userservice.domain.dto.request.UserPasswordUpdateRequestDto;
import com.userservice.domain.dto.response.UserResponseDto;
import com.userservice.web.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user-service")
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<UserResponseDto> referUser(
            Authentication authentication
    ) {
        return userService.referUser(authentication);
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

    @PatchMapping("/{user_id}/after-email-cf")
    public ResponseEntity<ResponseDto> afterEmailCf(
            @PathVariable(name = "user_id") Long userId
    ) {
        return userService.updateRoleAfterEmailCF(userId);
    }
}
