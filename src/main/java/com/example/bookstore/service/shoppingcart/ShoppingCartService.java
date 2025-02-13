package com.example.bookstore.service.shoppingcart;

import com.example.bookstore.dto.cartitem.CartItemRequestDto;
import com.example.bookstore.dto.cartitem.UpdateCartItemDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;

public interface ShoppingCartService {

    ShoppingCartResponseDto getShoppingCart(Long id);

    ShoppingCartResponseDto createCartItem(CartItemRequestDto cartItemRequestDto, User user);

    void deleteCartItem(Long cartItemId,User user);

    ShoppingCartResponseDto updateCartItem(Long cartItemId, UpdateCartItemDto quantity,User user);

    ShoppingCart getShoppingCartByUserId(User user);
}
