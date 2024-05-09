package com.itemservice.domain.vo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemRequest {

    @NotBlank
    private String itemName;

    @NotBlank
    private String price;

    @NotBlank
    private String details;

    @NotBlank
    private String stockQuantity;
}
