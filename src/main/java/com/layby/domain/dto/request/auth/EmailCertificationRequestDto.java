package com.layby.domain.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class EmailCertificationRequestDto {

    @NotBlank
    String id;

    @NotEmpty
    @Email
    private String email;

}
