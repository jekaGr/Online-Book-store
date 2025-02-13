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

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(target = "cartItems",
            expression = "java(cartItemsToCartItemResponseDto(shoppingCart.getCartItems()))")
    ShoppingCartResponseDto toShoppingCartResponseDto(ShoppingCart shoppingCart);

    default Set<CartItemResponseDto> cartItemsToCartItemResponseDto(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(cartItem -> cartItemToCartItemResponseDto(cartItem))
                .collect(Collectors.toSet());
    }

    CartItemResponseDto cartItemToCartItemResponseDto(CartItem cartItem);
}
