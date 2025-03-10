package com.example.bookstore.util;

import com.example.bookstore.dto.cartitem.CartItemRequestDto;
import com.example.bookstore.dto.cartitem.CartItemResponseDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import java.math.BigDecimal;
import java.util.Set;

public class ShoppingCartTestUtil {

    public static Book getBook() {
        return new Book()
                .setId(1L)
                .setTitle("Sample Book 1")
                .setAuthor("Author A")
                .setIsbn("978-1-23-456789-7")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("This is a sample book description.")
                .setCoverImage("cover image");
    }

    public static CartItemRequestDto getCartItemRequestDto(Long bookId, int quantity) {
        return new CartItemRequestDto()
                .setBookId(bookId)
                .setQuantity(quantity);
    }

    public static User getUser() {
        return new User()
                .setId(3L)
                .setEmail("user@i.ua")
                .setPassword("qwerty123")
                .setFirstName("John")
                .setLastName("Smith")
                .setShippingAddress("Ukraine");
    }

    public static ShoppingCart getShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        return shoppingCart
                .setId(user.getId())
                .setUser(user)
                .setCartItems(Set.of(getCartItem(shoppingCart,getBook())));
    }

    public static CartItem getCartItem(ShoppingCart shoppingCart, Book book) {
        return new CartItem()
                .setId(1L)
                .setShoppingCart(shoppingCart)
                .setBook(book)
                .setQuantity(6);

    }

    public static ShoppingCartResponseDto getShoppingCartResponseDto(ShoppingCart shoppingCart) {
        return new ShoppingCartResponseDto().setId(shoppingCart.getId())
                .setUserId(shoppingCart.getUser().getId())
                .setCartItems(Set.of(getCartItemResponseDto()));
    }

    public static CartItemResponseDto getCartItemResponseDto() {
        CartItem cartItem = getCartItem(getShoppingCart(getUser()),getBook());
        return new CartItemResponseDto()
                .setId(cartItem.getId())
                .setBookId(cartItem.getBook().getId())
                .setBookTitle(cartItem.getBook().getTitle())
                .setQuantity(cartItem.getQuantity());
    }
}
