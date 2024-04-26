package com.userservice.web.controller;

import com.userservice.domain.dto.ResponseDto;
import com.userservice.domain.vo.PhoneNumberUpdateRequest;
import com.userservice.domain.vo.UserPasswordUpdateRequest;
import com.userservice.domain.dto.UserDto;
import com.userservice.web.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{user_id}")
    public ResponseEntity<UserDto> referUser(
            @PathVariable(name = "user_id") Long userId
    ) {
        return userService.referUser(userId);
    }

    @PatchMapping("/{user_id}/update-phone-number")
    public ResponseEntity<ResponseDto> updatePhoneNumber(
            @PathVariable(name = "user_id") Long userId,
            @RequestBody @Valid PhoneNumberUpdateRequest dto
    ) {
        return userService.updatePhoneNumber(userId, dto);
    }

    @PatchMapping("/{user_id}/update-password")
    public ResponseEntity<ResponseDto> updatePassword(
            @PathVariable(name = "user_id") Long userId,
            @RequestBody @Valid UserPasswordUpdateRequest dto
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
