package com.example.bookstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity()).build();
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    @Sql(scripts = "classpath:database/book/remove-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_ValidRequestDto_Success() throws Exception {
        // given
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setAuthor("Test Author");
        createBookRequestDto.setPrice(BigDecimal.valueOf(12.99));
        createBookRequestDto.setTitle("Test Book");
        createBookRequestDto.setIsbn("12324564789");
        createBookRequestDto.setDescription("Test Description");
        createBookRequestDto.setCategoryIds(List.of(1L));

        BookDto expected = new BookDto();
        expected.setId(1L);
        expected.setAuthor(createBookRequestDto.getAuthor());
        expected.setPrice(createBookRequestDto.getPrice());
        expected.setTitle(createBookRequestDto.getTitle());
        expected.setIsbn(createBookRequestDto.getIsbn());
        expected.setDescription(createBookRequestDto.getDescription());
        expected.setCategoryIds(createBookRequestDto.getCategoryIds());

        String jsonRequest = objectMapper.writeValueAsString(createBookRequestDto);

        // when
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        // then
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Get all books")
    @Sql(scripts = {"classpath:database/book/remove-all-books.sql",
            "classpath:database/book/add-three-books.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void getAll_ShouldReturnAllBooks() throws Exception {
        // given
        List<BookDto> expected = new ArrayList<>();
        BookDto book1 = new BookDto().setId(1L)
                .setAuthor("Test author")
                .setTitle("Test book")
                .setPrice(BigDecimal.valueOf(40.99))
                .setDescription("Test description")
                .setIsbn("123456789")
                .setCategoryIds(List.of(1L));
        expected.add(book1);

        BookDto book2 = new BookDto().setId(2L)
                .setAuthor("Test author1")
                .setTitle("Test book1")
                .setPrice(BigDecimal.valueOf(30.99))
                .setDescription("Test description1")
                .setIsbn("123456798")
                .setCategoryIds(List.of(2L));
        expected.add(book2);

        BookDto book3 = new BookDto().setId(3L)
                .setAuthor("Test author2")
                .setTitle("Test book2")
                .setPrice(BigDecimal.valueOf(10.99))
                .setDescription("Test description2")
                .setIsbn("123456987")
                .setCategoryIds(List.of(3L));
        expected.add(book3);

        // when
        MvcResult result = mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andReturn();


        // then
        List<BookDto> actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<>() {});
        Assertions.assertEquals(3, actual.size());
        Assertions.assertEquals(expected, actual);
    }
}
