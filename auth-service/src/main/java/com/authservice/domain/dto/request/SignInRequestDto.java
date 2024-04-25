package com.authservice.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import jakarta.validation.constraints.Size;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequestDto {

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(min = 3, max = 100)
    private String password;
}
