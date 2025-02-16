package com.example.bookstore.controler;

import com.example.bookstore.dto.order.CreateOrderRequestDto;
import com.example.bookstore.dto.order.OrderDtoForUpdate;
import com.example.bookstore.dto.order.OrderResponseDto;
import com.example.bookstore.dto.orderitem.OrderItemResponseDto;
import com.example.bookstore.model.User;
import com.example.bookstore.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping({"/orders"})
@Tag(name = "Orders", description = "API for managing orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Place an order",
            description = "Creates a new order for the user"
    )
    public OrderResponseDto placeOrder(@RequestBody @Valid CreateOrderRequestDto requestDto,
                                       @AuthenticationPrincipal User user) {
        return orderService.createOrder(requestDto, user);
    }

    @GetMapping
    @Operation(
            summary = "Get user orders",
            description = "Retrieves all orders of the authenticated user"
    )
    public List<OrderResponseDto> getOrders(@AuthenticationPrincipal User user, Pageable pageable) {
        return orderService.getOrders(user, pageable);
    }

    @PatchMapping({"/{orderId}"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(
            summary = "Update order status",
            description = "Changes the status of an order"
    )
    public void updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody @Valid OrderDtoForUpdate dtoForUpdate,
            @AuthenticationPrincipal User user
    ) {
        orderService.changeOrderStatus(orderId, dtoForUpdate, user);
    }

    @GetMapping({"/{orderId}/items"})
    @Operation(
            summary = "Get items of an order",
            description = "Retrieves all items from a specific order"
    )
    public List<OrderItemResponseDto> getOrderItemsByOrderId(
            @PathVariable Long orderId,
            @AuthenticationPrincipal User user,
            Pageable pageable
    ) {
        return orderService.getOrderItems(orderId, user, pageable);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(
            summary = "Get an order item by ID",
            description = "Retrieves details of a specific item in an order"
    )

    public OrderItemResponseDto getOrderItemById(
            @PathVariable Long orderId,
            @PathVariable Long itemId,
            @AuthenticationPrincipal User user
    ) {
        return orderService.getOrderItem(orderId, itemId, user);
    }
}
