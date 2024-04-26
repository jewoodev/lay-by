package com.userservice.domain.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserPasswordUpdateRequest {

    @NotBlank
    private String password;
}
