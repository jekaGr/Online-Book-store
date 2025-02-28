package com.example.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Category;
import com.example.bookstore.repository.book.BookRepository;
import com.example.bookstore.repository.book.SpecificationBuilderImpl;
import com.example.bookstore.service.book.BookServiceImpl;
import com.example.bookstore.service.category.CategoryService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private CategoryService categoryService;

    @Mock
    private SpecificationBuilderImpl specificationBuilder;

    private Book book;
    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setPrice(BigDecimal.valueOf(15.99));
        book.setCategories(Set.of(new Category()));

        bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Test Book");
        bookDto.setAuthor("Test Author");
        bookDto.setPrice(BigDecimal.valueOf(15.99));
        bookDto.setCategoryIds(List.of());
    }

    @Test
    @DisplayName("Test if book is saved successfully")
    void save_ShouldReturnBookDto() {
        // Given
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Test Book");
        requestDto.setAuthor("Test Author");
        requestDto.setPrice(BigDecimal.valueOf(15.99));
        requestDto.setCategoryIds(List.of(1L));

        Category category = new Category();
        category.setId(1L);
        when(categoryService.getCategoriesByIdIn(requestDto.getCategoryIds()))
                .thenReturn(Set.of(category));
        when(bookMapper.toBook(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toBookDto(book)).thenReturn(bookDto);

        // When
        BookDto actual = bookService.save(requestDto);

        // Then
        assertEquals(bookDto, actual);
        verify(bookRepository).save(book);
    }

    @Test
    @DisplayName("Test if book is found by ID")
    void findById_ShouldReturnBookDto() {
        // Given
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toBookDto(book)).thenReturn(bookDto);

        // When
        BookDto actual = bookService.findById(bookId);

        // Then
        assertEquals(bookDto, actual);
    }

    @Test
    @DisplayName("Test if exception is thrown when book not found")
    void findById_WithNonExistentBook_ShouldThrowException() {
        // Given
        Long bookId = 100L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.findById(bookId));

        // Then
        assertEquals("Book not found with id " + bookId, exception.getMessage());
    }

    @Test
    @DisplayName("Test if findAll returns books")
    void findAll_ShouldReturnBooks() {
        // Given
        Pageable pageable = mock(Pageable.class);
        List<Book> books = List.of(book);
        Page<Book> bookPage = new PageImpl<>(books);
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toBookDto(book)).thenReturn(bookDto);

        // When
        Page<BookDto> actual = bookService.findAll(pageable);

        // Then
        assertNotNull(actual);
        assertEquals(1, actual.getTotalElements());
        assertEquals(bookDto, actual.getContent().get(0));
    }

    @Test
    @DisplayName("Test if book is updated by ID successfully")
    void updateById_WithValidCreateBookRequestDto_ShouldReturnUpdatedBookDto() {
        // Given
        Book existingBook = new Book();
        existingBook.setId(3L);
        existingBook.setTitle("The Trial");
        existingBook.setAuthor("Franz Kafka");
        existingBook.setPrice(BigDecimal.valueOf(77.77));
        existingBook.setCategories(Set.of());
        BigDecimal updatedPrice = BigDecimal.valueOf(88.88);
        CreateBookRequestDto bookDtoToUpdate = new CreateBookRequestDto();
        bookDtoToUpdate.setTitle(existingBook.getTitle());
        bookDtoToUpdate.setAuthor(existingBook.getAuthor());
        bookDtoToUpdate.setPrice(updatedPrice);
        bookDtoToUpdate.setCategoryIds(List.of());

        BookDto expected = new BookDto();
        expected.setTitle(bookDtoToUpdate.getTitle());
        expected.setAuthor(bookDtoToUpdate.getAuthor());
        expected.setPrice(bookDtoToUpdate.getPrice());
        expected.setCategoryIds(bookDtoToUpdate.getCategoryIds());

        when(bookRepository.findById(Mockito.any())).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(Mockito.any(Book.class))).thenReturn(existingBook);
        when(bookMapper.toBookDto(Mockito.any(Book.class))).thenReturn(expected);

        // When
        BookDto actual = bookService.updateById(3L, bookDtoToUpdate);

        // Then
        assertNotNull(actual);
        assertEquals(updatedPrice, actual.getPrice());

        verify(bookRepository).findById(Mockito.any());
        verify(bookRepository).save(Mockito.any(Book.class));
        verify(bookMapper).toBookDto(Mockito.any(Book.class));
    }
}
