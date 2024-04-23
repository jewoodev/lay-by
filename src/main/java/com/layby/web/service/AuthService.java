package com.layby.web.service;

import com.layby.domain.dto.request.auth.*;
import com.layby.domain.dto.response.ResponseDto;
import com.layby.domain.dto.response.auth.*;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<ResponseDto> usernameCheck(UsernameCheckRequestDto dto);

    ResponseEntity<ResponseDto> emailCertification(EmailCertificationRequestDto dto);

    ResponseEntity<ResponseDto> checkCertification(CheckCertificationRequestDto dto);

    ResponseEntity<ResponseDto> signUp(SignUpRequestDto dto);

    ResponseEntity<SignInResponseDto> signIn(SignInRequestDto dto);
}
