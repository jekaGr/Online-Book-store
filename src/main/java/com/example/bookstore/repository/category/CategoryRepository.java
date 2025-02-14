package com.example.bookstore.repository.category;

import com.example.bookstore.model.Category;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Set<Category> getCategoriesByIdIn(List<Long> list);
}
