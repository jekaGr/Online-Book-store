package com.example.bookstore.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.shoppingcart.ShoppingCartRepository;
import com.example.bookstore.util.ShoppingCartTestUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
public class ShoppingCartRepositoryTest {
    @Autowired
    private ShoppingCartRepository cartRepository;

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/delete-all-data-before-tests.sql"));
        }
    }

    @Test
    @Sql(
            scripts = "classpath:database/insert-data-ci_repository_test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/delete-all-data-before-tests.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void findByUserId_ShouldReturnShoppingCart() {
        //Given
        Book book = ShoppingCartTestUtil.getBook();
        User user = ShoppingCartTestUtil.getUser();
        ShoppingCart shoppingCart = ShoppingCartTestUtil.getShoppingCart(user);
        CartItem cartItem = ShoppingCartTestUtil.getCartItem(shoppingCart, book);
        ShoppingCart expected = shoppingCart.setCartItems(Set.of(cartItem));

        //When
        ShoppingCart actual = cartRepository.findByUserId(3L)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find a shopping cart by userId " + 3L));

        //Then
        assertNotNull(actual);
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("user", "cartItems")
                .isEqualTo(expected);
    }
}
