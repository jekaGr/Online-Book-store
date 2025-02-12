package com.example.bookstore.mapper;

import com.example.bookstore.config.MapperConfig;
import com.example.bookstore.dto.cartitem.CartItemResponseDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.ShoppingCart;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    @Mapping(target = "cartItems",
            expression = "java(cartItemsToCartItemResponseDto(shoppingCart.getCartItems()))")
    ShoppingCartResponseDto toShoppingCartResponseDto(ShoppingCart shoppingCart);

    default Set<CartItemResponseDto> cartItemsToCartItemResponseDto(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::cartItemToCartItemResponseDto)
                .collect(Collectors.toSet());
    }

    default CartItemResponseDto cartItemToCartItemResponseDto(CartItem cartItem) {
        CartItemResponseDto responseDto = new CartItemResponseDto();
        responseDto.setId(cartItem.getId());
        responseDto.setBookId(cartItem.getBook().getId());
        responseDto.setBookTitle(cartItem.getBook().getTitle());
        responseDto.setQuantity(cartItem.getQuantity());
        return responseDto;
    }
}
