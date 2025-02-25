package com.example.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.bookstore.model.Category;
import com.example.bookstore.repository.category.CategoryRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void findByName_GivenExistingCategory_ShouldReturnCategory() {
        // given
        Category category = new Category();
        category.setName("Testing Category");
        categoryRepository.save(category);
        // when
        Optional<Category> foundCategory = categoryRepository.findById(category.getId());
        // then
        assertTrue(foundCategory.isPresent());
        assertEquals("Testing Category", foundCategory.get().getName());
    }
}
