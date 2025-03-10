package com.example.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.bookstore.dto.cartitem.CartItemRequestDto;
import com.example.bookstore.dto.cartitem.UpdateCartItemDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.ShoppingCartMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.book.BookRepository;
import com.example.bookstore.repository.cartitem.CartItemRepository;
import com.example.bookstore.repository.shoppingcart.ShoppingCartRepository;
import com.example.bookstore.repository.user.UserRepository;
import com.example.bookstore.service.shoppingcart.ShoppingCartServiceImpl;
import com.example.bookstore.util.ShoppingCartTestUtil;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    private Book testBook;
    private User testUser;
    private ShoppingCart testShoppingCart;
    private CartItem testCartItem;
    private ShoppingCartResponseDto testShoppingCartResponseDto;
    private CartItemRequestDto testCartItemRequestDto;

    @BeforeEach
    public void setUp() {
        testBook = ShoppingCartTestUtil.getBook();
        testUser = ShoppingCartTestUtil.getUser();
        testShoppingCart = ShoppingCartTestUtil.getShoppingCart(testUser);
        testCartItem = ShoppingCartTestUtil.getCartItem(testShoppingCart, testBook);
        testShoppingCartResponseDto = ShoppingCartTestUtil
                .getShoppingCartResponseDto(testShoppingCart);
        testCartItemRequestDto = ShoppingCartTestUtil.getCartItemRequestDto(testBook.getId(),2);
        testShoppingCart.setCartItems(Set.of(testCartItem));

        ReflectionTestUtils.setField(shoppingCartService,
                "shoppingCartRepository", shoppingCartRepository);
        ReflectionTestUtils.setField(shoppingCartService,
                "cartItemRepository", cartItemRepository);
    }

    @Test
    public void testCreateShoppingCart() {
        // Given
        when(shoppingCartRepository.save(any(ShoppingCart.class)))
                .thenReturn(testShoppingCart);

        // When
        shoppingCartService.createShoppingCart(testUser);

        // Then
        verify(shoppingCartRepository).save(any(ShoppingCart.class));
    }

    @Test
    void testCreateCartItem_ItemAlreadyInCart() {
        // Given
        CartItem existingCartItem = testShoppingCart.getCartItems().iterator().next();
        existingCartItem.setQuantity(6);

        when(shoppingCartRepository.findByUserId(testUser.getId()))
                .thenReturn(Optional.of(testShoppingCart));
        when(shoppingCartMapper.toShoppingCartResponseDto(testShoppingCart))
                .thenReturn(testShoppingCartResponseDto);

        // When
        ShoppingCartResponseDto result = shoppingCartService.createCartItem(
                testCartItemRequestDto, testUser);

        // Then
        assertNotNull(result);
        assertEquals(8, existingCartItem.getQuantity());
        verify(cartItemRepository).save(existingCartItem);
    }

    @Test
    void testGetShoppingCartByUserId_UserFound() {
        // Given
        when(shoppingCartMapper.toShoppingCartResponseDto(testShoppingCart))
                .thenReturn(ShoppingCartTestUtil.getShoppingCartResponseDto(testShoppingCart));
        when(shoppingCartRepository.findById(testUser.getId()))
                .thenReturn(Optional.of(testShoppingCart));

        // When
        ShoppingCartResponseDto result = shoppingCartService.getShoppingCart(testUser.getId());

        // Then
        assertNotNull(result);
        assertEquals(testShoppingCart.getId(), result.getId());
        assertEquals(testShoppingCart.getUser().getId(), result.getUserId());
        verify(shoppingCartRepository).findById(testUser.getId());
    }

    @Test
    public void testUpdateCartItem() {
        // Given
        UpdateCartItemDto updateCartItemDto = new UpdateCartItemDto();
        updateCartItemDto.setQuantity(10);
        when(shoppingCartRepository.findByUserId(testUser.getId()))
                .thenReturn(Optional.of(testShoppingCart));
        when(cartItemRepository.findByIdAndShoppingCartId(testCartItem.getId(),
                testShoppingCart.getId()))
                .thenReturn(Optional.of(testCartItem));
        when(shoppingCartMapper.toShoppingCartResponseDto(testShoppingCart))
                .thenReturn(testShoppingCartResponseDto);

        // When
        ShoppingCartResponseDto response = shoppingCartService.updateCartItem(
                testCartItem.getId(), updateCartItemDto, testUser);

        // Then
        assertNotNull(response);
        assertEquals(10, testCartItem.getQuantity());
        verify(cartItemRepository).save(testCartItem);
    }

    @Test
    public void testUpdateCartItem_CartItemNotFound() {
        // Given
        UpdateCartItemDto updateCartItemDto = new UpdateCartItemDto();
        updateCartItemDto.setQuantity(10);
        when(shoppingCartRepository.findByUserId(testUser.getId()))
                .thenReturn(Optional.of(testShoppingCart));
        when(cartItemRepository.findByIdAndShoppingCartId(testCartItem.getId(),
                testShoppingCart.getId()))
                .thenReturn(Optional.empty());

        // When
        assertThrows(EntityNotFoundException.class, () -> {
            shoppingCartService.updateCartItem(testCartItem.getId(), updateCartItemDto, testUser);
        });
    }
}
