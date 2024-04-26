package com.userservice.web.service;

import com.userservice.domain.dto.auth.TokenDto;
import com.userservice.domain.vo.auth.*;
import com.userservice.domain.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<ResponseDto> usernameCheck(UsernameCheckRequest dto);

    ResponseEntity<ResponseDto> emailCertification(EmailCertificationRequest dto);

    ResponseEntity<ResponseDto> checkCertification(CheckCertificationRequest dto);

    ResponseEntity<ResponseDto> signUp(SignUpRequest dto);

    TokenDto signIn(SignInRequest dto);

    ResponseEntity<ResponseDto> logout(Long userId);
}
