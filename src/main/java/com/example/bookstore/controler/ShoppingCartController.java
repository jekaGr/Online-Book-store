package com.example.bookstore.controler;

import com.example.bookstore.dto.cartitem.CartItemRequestDto;
import com.example.bookstore.dto.cartitem.UpdateCartItemDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.bookstore.model.User;
import com.example.bookstore.service.shoppingcart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ShoppingCart management", description = "Endpoints of management ShoppingCart.")
@RequestMapping("/cart")
@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @Operation(
            summary = "Get user's shopping cart",
            description = "Retrieves the shopping cart of the authenticated user"
    )
    public ShoppingCartResponseDto getShoppingCart(
            @AuthenticationPrincipal User user
    ) {
        return shoppingCartService.getShoppingCart(user.getId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Add item",
            description = "Add item to shopping cart"
    )
    public ShoppingCartResponseDto createCartItem(
            @RequestBody @Valid CartItemRequestDto cartItemRequestDto,
            @AuthenticationPrincipal User user
    ) {
        return shoppingCartService.createCartItem(cartItemRequestDto, user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/items/{cartItemId}")
    @Operation(
            summary = "Remove item",
            description = "Delete item from shopping cart"
    )
    public void deleteCartItem(
            @PathVariable Long cartItemId,
            @AuthenticationPrincipal User user
    ) {
        shoppingCartService.deleteCartItem(cartItemId,user);
    }

    @PutMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Update item",
            description = "Update quantity"
    )
    public ShoppingCartResponseDto updateCartItem(
            @PathVariable Long cartItemId,
            @RequestBody @Valid UpdateCartItemDto dtoForUpdate,
            @AuthenticationPrincipal User user) {
        return shoppingCartService.updateCartItem(cartItemId,dtoForUpdate,user);
    }
}
