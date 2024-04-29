package com.userservice.domain.vo.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class UsernameCheckRequest {

    @NotBlank
    private String username;
}
