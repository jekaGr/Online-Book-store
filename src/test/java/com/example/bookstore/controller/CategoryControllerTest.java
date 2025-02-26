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

import com.example.bookstore.dto.category.CategoryRequestDto;
import com.example.bookstore.dto.category.CategoryResponseDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.service.category.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
class CategoryControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private CategoryService categoryService;

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

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Create a new category as admin")
    void createCategory_ShouldReturnCategoryResponseDto() throws Exception {
        // Given
        CategoryRequestDto requestDto = new CategoryRequestDto().setName("category4")
                .setDescription("description4");

        CategoryResponseDto expected = new CategoryResponseDto().setId(4L)
                .setName("category4").setDescription("description4");
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        // When
        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        // Then
        CategoryResponseDto actual = objectMapper.readValue(result.getResponse()
                        .getContentAsString(), CategoryResponseDto.class);
        assertNotNull(actual);
        assertTrue(reflectionEquals(expected, actual));
    }

    @Test
    @WithMockUser(username = "admin@mail.com", roles = {"ADMIN", "USER"})
    void getCategoryById_ShouldReturnCategory() throws Exception {
        // given
        CategoryResponseDto expected = new CategoryResponseDto().setId(1L).setName("category1")
                .setDescription("description1");
        // when
        MvcResult result = mockMvc.perform(get("/categories/{id}",1L)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(),
                        CategoryResponseDto.class);

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "admin@mail.com", roles = {"ADMIN", "USER"})
    @Test
    void getAll_GivenCategoriesInCatalog_ShouldReturnAllCategories() throws Exception {
        // given
        List<CategoryResponseDto> expected = new ArrayList<>();
        expected.add(new CategoryResponseDto().setId(1L)
                .setName("category1")
                .setDescription("category desc1"));
        expected.add(new CategoryResponseDto().setId(2L)
                .setName("category2")
                .setDescription("category desc2"));
        expected.add(new CategoryResponseDto().setId(3L)
                .setName("category3")
                .setDescription("category desc3"));

        // when
        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        // then
        CategoryResponseDto[] actual = objectMapper.readValue(result.getResponse()
                        .getContentAsString(), CategoryResponseDto[].class);
        Assertions.assertEquals(expected.size(), actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Delete category by id as admin")
    void delete_ShouldReturnNoContent() throws Exception {
        // Given
        Long expectedId = 1L;
        // When
        mockMvc.perform(delete("/categories/{id}", expectedId)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        // Then
        assertThrows(EntityNotFoundException.class,() -> categoryService.getById(expectedId));
    }
}
