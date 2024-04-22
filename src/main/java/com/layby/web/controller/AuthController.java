package com.layby.web.controller;

import com.layby.domain.dto.request.auth.*;
import com.layby.domain.dto.response.ResponseDto;
import com.layby.domain.dto.response.auth.*;

import com.layby.web.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/usename-check")
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
}
