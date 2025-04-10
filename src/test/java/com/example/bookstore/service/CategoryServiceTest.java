package com.example.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.bookstore.dto.category.CategoryRequestDto;
import com.example.bookstore.dto.category.CategoryResponseDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.CategoryMapper;
import com.example.bookstore.model.Category;
import com.example.bookstore.repository.category.CategoryRepository;
import com.example.bookstore.service.category.CategoryServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void getById_WithNonExisingCategory_ShouldThrowException() {
        // Given
        Long categoryId = 100L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        // When
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.getById(categoryId)
        );
        // Then
        String expected = "Can't get a category by id: " + categoryId;
        String actual = exception.getMessage();
        assertEquals(expected, actual);
        verify(categoryRepository).findById(categoryId);
    }

    @Test
    void getById_WithValidCategory_ShouldReturnCategory() {
        // Given
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Horror");
        category.setDescription("Scary books.");

        CategoryResponseDto expected = new CategoryResponseDto();
        expected.setId(category.getId());
        expected.setName(category.getName());
        expected.setDescription(category.getDescription());

        when(categoryRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(category));
        when(categoryMapper.toDto(Mockito.any(Category.class))).thenReturn(expected);
        // When
        CategoryResponseDto actual = categoryService.getById(categoryId);
        // Then
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(categoryRepository).findById(categoryId);
    }

    @Test
    public void findAll_ValidPageable_ReturnsAllCategories() {
        // Given
        Category category = new Category();
        category.setId(1L);
        category.setName("Horror");
        category.setDescription("Scary books.");

        CategoryResponseDto categoryDto = new CategoryResponseDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());

        Pageable pageable = PageRequest.of(0, 10);
        List<Category> categories = List.of(category);
        Page<Category> categoryPage = new PageImpl<>(categories, pageable, 5);

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        // When
        List<CategoryResponseDto> categoriesDto = categoryService.getAll(pageable);

        // Then
        assertEquals(1,categoriesDto.size());
        assertEquals(categoryDto,categoriesDto.get(0));

        verify(categoryRepository).findAll(pageable);
        verify(categoryMapper).toDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    void update_WithValidCategoryRequestDto_ShouldReturnCategoryDto() {
        // Given
        Category existingCategory = new Category();
        existingCategory.setId(3L);
        existingCategory.setName("History");
        existingCategory.setDescription("History books");

        String updatedName = "Updated History";
        String updatedDescription = "Updated History books";

        CategoryRequestDto categoryDtoToUpdate = new CategoryRequestDto();
        categoryDtoToUpdate.setName(updatedName);
        categoryDtoToUpdate.setDescription(updatedDescription);

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setId(existingCategory.getId());
        categoryResponseDto.setName(updatedName);
        categoryResponseDto.setDescription(updatedDescription);

        when(categoryRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(Mockito.any(Category.class)))
                .thenReturn(existingCategory);
        when(categoryMapper.toDto(Mockito.any(Category.class)))
                .thenReturn(categoryResponseDto);

        // When
        CategoryResponseDto updatedCategoryDto = categoryService.update(3L, categoryDtoToUpdate);

        // Then
        assertNotNull(updatedCategoryDto);
        assertEquals(updatedName, updatedCategoryDto.getName());
        assertEquals(updatedDescription, updatedCategoryDto.getDescription());

        verify(categoryRepository).save(Mockito.any(Category.class));
        verify(categoryMapper).updateCategory(Mockito.any(Category.class),
                Mockito.any(CategoryRequestDto.class));
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }
}
