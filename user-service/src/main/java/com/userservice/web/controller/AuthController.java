package com.userservice.web.controller;

import com.userservice.domain.dto.auth.TokenDto;
import com.userservice.domain.vo.auth.*;
import com.userservice.domain.dto.ResponseDto;
import com.userservice.web.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/username-check")
    public ResponseEntity<ResponseDto> usernameCheck(
            @RequestBody @Valid UsernameCheckRequest requestBody
        ) {
        ResponseEntity<ResponseDto> response = authService.usernameCheck(requestBody);
        return response;
    }

    @PostMapping("/email-certification")
    public ResponseEntity<ResponseDto> emailCertification(
            @RequestBody @Valid EmailCertificationRequest requestBody
    ) {
        return authService.emailCertification(requestBody);
    }

    @PostMapping("/check-certification")
    public ResponseEntity<ResponseDto> checkCertification(
            @RequestBody @Valid CheckCertificationRequest requestBody
    ) {
        return authService.checkCertification(requestBody);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDto> signUp(
            @RequestBody @Valid SignUpRequest requestBody
    ) {
        return authService.signUp(requestBody);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ResponseDto> signIn(
            @RequestBody @Valid SignInRequest requestBody,
            HttpServletResponse response
    ) {
        TokenDto tokenDto = authService.signIn(requestBody);
        response.addHeader("Authorization", tokenDto.getToken());
        response.addHeader("uuid", tokenDto.getUuid());

        return ResponseDto.success();
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<ResponseDto> logout(@PathVariable(name = "user_id") Long userId) {
        return authService.logout(userId);
    }
}
