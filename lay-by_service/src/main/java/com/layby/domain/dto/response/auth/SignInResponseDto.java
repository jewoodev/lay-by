package com.layby.domain.dto.response.auth;

import com.layby.domain.dto.response.ResponseDto;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
public class SignInResponseDto {

    private String token;
}
