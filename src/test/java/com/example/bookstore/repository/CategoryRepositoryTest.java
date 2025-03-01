package com.example.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.bookstore.model.Category;
import com.example.bookstore.repository.category.CategoryRepository;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void getCategoriesByIdIn_ShouldReturnCategoriesForGivenIds() {
        // given
        Category category = new Category();
        category.setName("Category 1");
        categoryRepository.save(category);

        Category categoryOne = new Category();
        categoryOne.setName("Category 2");
        categoryRepository.save(categoryOne);

        List<Long> ids = List.of(category.getId(), categoryOne.getId());

        // when
        Set<Category> categories = categoryRepository.getCategoriesByIdIn(ids);

        // then
        assertEquals(2, categories.size());
        assertTrue(categories.stream().anyMatch(c -> c.getName().equals("Category 1")));
        assertTrue(categories.stream().anyMatch(c -> c.getName().equals("Category 2")));
    }
}
