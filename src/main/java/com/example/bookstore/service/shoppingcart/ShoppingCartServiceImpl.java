package com.example.bookstore.service.shoppingcart;

import com.example.bookstore.dto.cartitem.CartItemRequestDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.ShoppingCartMapper;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.book.BookRepository;
import com.example.bookstore.repository.cartitem.CartItemRepository;
import com.example.bookstore.repository.shoppingcart.ShoppingCartRepository;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartResponseDto getShoppingCart(Long id) {
        return shoppingCartMapper.toShoppingCartResponseDto(shoppingCartRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                "Can't find shopping cart by user Id " + id)));
    }

    @Transactional
    @Override
    public ShoppingCartResponseDto createCartItem(CartItemRequestDto cartItemRequestDto,
                                                  User user) {
        Long userId = user.getId();
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    ShoppingCart newShoppingCart = new ShoppingCart();
                    newShoppingCart.setUser(user);
                    newShoppingCart.setCartItems(new HashSet<>());
                    shoppingCartRepository.save(newShoppingCart);
                    return newShoppingCart;
                });
        CartItem cartItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(cartItemRequestDto.getBookId()))
                .findFirst()
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + cartItemRequestDto.getQuantity());
        } else {
            cartItem = new CartItem();
            cartItem.setShoppingCart(shoppingCart);
            cartItem.setBook(bookRepository.getReferenceById(cartItemRequestDto.getBookId()));
            cartItem.setQuantity(cartItemRequestDto.getQuantity());
            shoppingCart.getCartItems().add(cartItem);
        }
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toShoppingCartResponseDto(shoppingCart);
    }

    @Override
    public void deleteCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public ShoppingCartResponseDto updateCartItem(Long cartItemId,int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException("Can't find shopping cart by user Id "
                        + cartItemId));
        cartItem.setQuantity(quantity);
        return null;
    }
}
