package com.example.bookstore.controller;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.junit.Assert.assertEquals;
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
import com.example.bookstore.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
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
        CategoryRequestDto requestDto = TestUtil.createCategoryRequestDto();

        CategoryResponseDto expected = TestUtil.createCategoryResponseDto();
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
        CategoryResponseDto expected = TestUtil.getCategoryResponseDto();
        // when
        MvcResult result = mockMvc.perform(get("/categories/{id}",1L)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(),
                        CategoryResponseDto.class);

        assertNotNull(actual);
        assertTrue(reflectionEquals(expected, actual));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Get category by non-existent id as user")
    void getCategoryById_NonExistentId_ShouldReturnNotFound() throws Exception {
        // Given
        Long invalidId = 999L;
        // When
        mockMvc.perform(get("/categories/{id}", invalidId)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "admin@mail.com", roles = {"ADMIN", "USER"})
    @Test
    void getAll_GivenCategoriesInCatalog_ShouldReturnAllCategories() throws Exception {
        // given
        List<CategoryResponseDto> expected = TestUtil.getCategoryResponseDtoList();

        // when
        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        // then
        CategoryResponseDto[] actual = objectMapper.readValue(result.getResponse()
                        .getContentAsString(), CategoryResponseDto[].class);
        assertEquals(expected.size(), actual.length);
        assertEquals(expected, Arrays.stream(actual).toList());
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
