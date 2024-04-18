package com.layby.web.service;

import com.layby.domain.dto.request.auth.EmailCertificationRequestDto;
import com.layby.domain.dto.response.auth.EmailCertificationResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto);
}
