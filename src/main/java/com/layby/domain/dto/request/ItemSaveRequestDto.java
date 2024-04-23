package com.layby.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemSaveRequestDto {

    @NotBlank
    private String itemName;

    @NotBlank
    private String price;

    @NotBlank
    private String details;

    @NotBlank
    private String stockQuantity;
}
