package com.userservice.domain.vo.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
public class SignUpRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(min = 3, max = 100)
    private String password;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phoneNumber;
}
