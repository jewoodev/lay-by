package com.itemservice.domain.vo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
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
