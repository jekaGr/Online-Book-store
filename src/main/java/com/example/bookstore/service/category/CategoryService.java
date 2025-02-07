package com.example.bookstore.service.category;

import com.example.bookstore.dto.category.CategoryRequestDto;
import com.example.bookstore.dto.category.CategoryResponseDto;
import com.example.bookstore.model.Category;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryResponseDto> getAll(Pageable pageable);

    CategoryResponseDto getById(Long id);

    CategoryResponseDto save(CategoryRequestDto categoryRequestDto);

    CategoryResponseDto update(Long id, CategoryRequestDto categoryRequestDto);

    void deleteById(Long id);

    Set<Category> getCategoriesByIdIn(List<Long> list);
}
