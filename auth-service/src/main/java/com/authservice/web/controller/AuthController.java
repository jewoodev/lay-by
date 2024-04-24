package com.authservice.web.controller;

import com.authservice.domain.dto.request.*;
import com.authservice.domain.dto.response.ResponseDto;
import com.authservice.domain.dto.response.SignInResponseDto;
import com.authservice.web.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth-service")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/username-check")
    public ResponseEntity<ResponseDto> usernameCheck(
            @RequestBody @Valid UsernameCheckRequestDto requestBody
        ) {
        ResponseEntity<ResponseDto> response = authService.usernameCheck(requestBody);
        return response;
    }

    @PostMapping("/email-certification")
    public ResponseEntity<ResponseDto> emailCertification(
            @RequestBody @Valid EmailCertificationRequestDto requestBody
    ) {
        return authService.emailCertification(requestBody);
    }

    @PostMapping("/check-certification")
    public ResponseEntity<ResponseDto> checkCertification(
            @RequestBody @Valid CheckCertificationRequestDto requestBody
    ) {
        return authService.checkCertification(requestBody);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDto> signUp(
            @RequestBody @Valid SignUpRequestDto requestBody
    ) {
        return authService.signUp(requestBody);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponseDto> signIn(
            @RequestBody @Valid SignInRequestDto requestBody
    ) {
        return authService.signIn(requestBody);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logout(Authentication authentication) {
        return authService.logout(authentication);
    }
}
