package com.example.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Category;
import com.example.bookstore.repository.book.BookRepository;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    void findById_GivenExistingBook_ShouldReturnBook() {
        // given
        Book book = new Book();
        book.setTitle("Testing Book");
        book.setAuthor("Author Name");
        book.setIsbn("ISBN");
        book.setPrice(BigDecimal.valueOf(15.99));
        book.setCategories(Set.of(new Category()));
        bookRepository.save(book);
        // when
        Optional<Book> foundBook = bookRepository.findById(book.getId());
        // then
        assertTrue(foundBook.isPresent());
        assertEquals("Testing Book", foundBook.get().getTitle());
    }

}
