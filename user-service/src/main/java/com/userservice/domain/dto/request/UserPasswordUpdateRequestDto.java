package com.userservice.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserPasswordUpdateRequestDto {

    @NotBlank
    private String password;
}
