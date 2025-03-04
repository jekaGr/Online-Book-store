package com.example.bookstore.dto.order;

import com.example.bookstore.model.Order;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderDtoForUpdate {
    @NotNull
    private Order.Status status;
}
