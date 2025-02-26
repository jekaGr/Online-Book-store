package com.example.bookstore.controller;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.service.book.BookService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity()).build();
    }

    @BeforeEach
    void beforeEach(@Autowired DataSource dataSourced) throws SQLException {
        teardown(dataSourced);
        try (Connection connection = dataSourced.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/add-three-books.sql"));
        }
    }

    @SneakyThrows
    private static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/remove-all-books.sql")
            );
        }
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    void createBook_ValidRequestDto_Success() throws Exception {
        // given
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setAuthor("Test Author3");
        createBookRequestDto.setPrice(BigDecimal.valueOf(12.99));
        createBookRequestDto.setTitle("Test Book3");
        createBookRequestDto.setIsbn("12324564897");
        createBookRequestDto.setDescription("Test Description3");
        createBookRequestDto.setCategoryIds(List.of(1L));

        BookDto expected = new BookDto();
        expected.setId(4L);
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
        MvcResult result = mockMvc.perform(get("/books")
                         .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                 .andExpect(status().isOk()).andReturn();

        // then
        String json = result.getResponse().getContentAsString();
        Map<String, Object> map = objectMapper.readValue(json, new TypeReference<>() {});

        List<BookDto> books = objectMapper.convertValue(map.get("content"),
                new TypeReference<>() {});

        Assertions.assertEquals(3, books.size());
        Assertions.assertEquals(expected, books);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Get book by id as user")
    void getBookById_ValidId_ShouldReturnBookDto() throws Exception {
        // Given
        BookDto expected = new BookDto().setId(1L)
                .setAuthor("Test author")
                .setTitle("Test book")
                .setPrice(BigDecimal.valueOf(40.99))
                .setDescription("Test description")
                .setIsbn("123456789")
                .setCategoryIds(List.of(1L));
        // When
        MvcResult result = mockMvc.perform(get("/books/{id}",1L)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        // Then
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertTrue(reflectionEquals(expected, actual));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Delete book by id as admin")
    void delete_ValidId_ShouldReturnNoContent() throws Exception {
        // Given
        Long expectedId = 1L;
        // When
        mockMvc.perform(delete("/books/{id}", expectedId)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        // Then
        assertThrows(EntityNotFoundException.class,() -> bookService.findById(expectedId));
    }
}
