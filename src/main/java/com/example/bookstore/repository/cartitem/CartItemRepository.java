package com.example.bookstore.repository.cartitem;

import com.example.bookstore.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByIdAndShoppingCartId(Long cartItemId, Long shoppingCartId);

    @Query("UPDATE CartItem c SET c.isDeleted = true WHERE c.shoppingCart.id = :userId")
    void deleteByShoppingCartAndId(Long userId);
}
