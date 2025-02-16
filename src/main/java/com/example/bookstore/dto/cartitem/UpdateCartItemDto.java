package com.example.bookstore.dto.cartitem;

import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class UpdateCartItemDto {
    @Positive
    private int quantity;
}
