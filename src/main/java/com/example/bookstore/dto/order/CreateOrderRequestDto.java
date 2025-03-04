package com.example.bookstore.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateOrderRequestDto {
    @NotBlank
    private String shippingAddress;
}
