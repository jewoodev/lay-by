package com.layby.domain.dto.request.auth;

import lombok.*;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequestDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String username;

    @NotNull
    @Size(min = 3, max = 100)
    private String password;
}
