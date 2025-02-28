package com.example.bookstore.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Category;
import com.example.bookstore.repository.book.BookRepository;
import com.example.bookstore.repository.category.CategoryRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void findAllByCategoryId_ShouldReturnBooksForCategory() {
        // given
        Category category = new Category().setName("Category Name");
        categoryRepository.save(category);

        List<Book> expected = new ArrayList<>();
        Book bookOne = new Book()
                .setTitle("Book 1")
                .setAuthor("Author 1")
                .setPrice(BigDecimal.valueOf(19.99))
                .setIsbn("ISBN 1")
                .setCategories(Set.of(category));
        bookRepository.save(bookOne);
        expected.add(bookOne);

        Book bookTwo = new Book()
                .setTitle("Book 2")
                .setAuthor("Author 2")
                .setPrice(BigDecimal.valueOf(22.99))
                .setIsbn("ISBN 2")
                .setCategories(Set.of(category));
        bookRepository.save(bookTwo);
        expected.add(bookTwo);

        // when
        List<Book> books = bookRepository.findAllByCategoryId(category.getId(), Pageable.unpaged());

        // then
        assertNotNull(books);
        assertEquals(2, books.size());
        assertTrue(books.stream().anyMatch(b -> b.getTitle().equals("Book 1")));
        assertTrue(books.stream().anyMatch(b -> b.getTitle().equals("Book 2")));
    }

}
