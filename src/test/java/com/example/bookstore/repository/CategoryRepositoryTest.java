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
        Category category1 = new Category();
        category1.setName("Category 1");
        categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setName("Category 2");
        categoryRepository.save(category2);

        Category category3 = new Category();
        category3.setName("Category 3");
        categoryRepository.save(category3);

        List<Long> ids = List.of(category1.getId(), category2.getId());

        // when
        Set<Category> categories = categoryRepository.getCategoriesByIdIn(ids);

        // then
        assertEquals(2, categories.size());
        assertTrue(categories.stream().anyMatch(c -> c.getName().equals("Category 1")));
        assertTrue(categories.stream().anyMatch(c -> c.getName().equals("Category 2")));
    }
}
