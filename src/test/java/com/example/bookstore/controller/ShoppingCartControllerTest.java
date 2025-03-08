package com.example.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstore.dto.cartitem.CartItemRequestDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.service.shoppingcart.ShoppingCartService;
import com.example.bookstore.util.ShoppingCartTestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShoppingCartControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity()).build();

    }

    @Test
    @WithUserDetails(value = "user@i.ua",
            userDetailsServiceBeanName = "customUserDetailsService")
    @Sql(
            scripts = {"classpath:database/insert-data-ci_repository_test.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/delete-all-data-before-tests.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void getShoppingCart_ValidId_ShouldReturnShoppingCartResponseDto() throws Exception {
        // Given
        User testUser = ShoppingCartTestUtil.getUser();
        ShoppingCart testShoppingCart = ShoppingCartTestUtil.getShoppingCart(testUser);
        ShoppingCartResponseDto expected = ShoppingCartTestUtil
                .getShoppingCartResponseDto(testShoppingCart);
        // When
        MvcResult result = mockMvc.perform(get("/cart")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        ShoppingCartResponseDto responseDto = objectMapper
                .readValue(responseBody, ShoppingCartResponseDto.class);
        assertNotNull(responseBody);
        assertTrue(responseBody.contains(testUser.getId().toString()));
        assertEquals(expected.getId(), responseDto.getId());
    }

    @Test
    @WithUserDetails(value = "user@i.ua",
            userDetailsServiceBeanName = "customUserDetailsService")
    @Sql(
            scripts = {"classpath:database/insert-data-ci_repository_test.sql",
                    "classpath:database/insert-into-cart_items.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/delete-all-data-before-tests.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void createCartItem_ValidRequest_ShouldReturnCreatedShoppingCartResponseDto() throws Exception {
        User testUser = ShoppingCartTestUtil.getUser();
        CartItemRequestDto cartItemRequestDto = ShoppingCartTestUtil
                .getCartItemRequestDto(4L, 2);
        ShoppingCart shoppingCart = ShoppingCartTestUtil.getShoppingCart(testUser);
        ShoppingCartResponseDto expected = ShoppingCartTestUtil
                .getShoppingCartResponseDto(shoppingCart);

        String jsonRequest = objectMapper.writeValueAsString(cartItemRequestDto);

        // When
        MvcResult result = mockMvc.perform(post("/cart")
                        .content(jsonRequest)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ShoppingCartResponseDto responseDto = objectMapper
                .readValue(responseBody, ShoppingCartResponseDto.class);
        // Then
        assertNotNull(responseDto, "Response DTO should not be null");
        assertNotNull(responseBody, "Response body should not be null");

        assertEquals(expected.getId(), responseDto.getId(), "Cart ID should match");
    }
}
