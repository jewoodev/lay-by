package com.authservice.web.service;


import com.authservice.domain.dto.request.*;
import com.authservice.domain.dto.response.ResponseDto;
import com.authservice.domain.dto.response.SignInResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AuthService {

    ResponseEntity<ResponseDto> usernameCheck(UsernameCheckRequestDto dto);

    ResponseEntity<ResponseDto> emailCertification(EmailCertificationRequestDto dto);

    ResponseEntity<ResponseDto> checkCertification(CheckCertificationRequestDto dto);

    ResponseEntity<ResponseDto> signUp(SignUpRequestDto dto);

    ResponseEntity<SignInResponseDto> signIn(SignInRequestDto dto);

    ResponseEntity<ResponseDto> logout(Authentication authentication);
}
