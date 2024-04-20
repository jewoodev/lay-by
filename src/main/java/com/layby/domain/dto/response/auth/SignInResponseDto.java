package com.layby.domain.dto.response.auth;

import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponseDto {

    private String token;

}
