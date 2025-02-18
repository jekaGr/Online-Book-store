package com.example.bookstore.service.book;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.book.BookSearchParameters;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Category;
import com.example.bookstore.repository.book.BookRepository;
import com.example.bookstore.repository.book.SpecificationBuilderImpl;
import com.example.bookstore.service.category.CategoryService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final SpecificationBuilderImpl specificationBuilder;
    private final CategoryService categoryService;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Set<Category> categories
                = categoryService.getCategoriesByIdIn(requestDto.getCategoryIds());
        Book book = bookMapper.toBook(requestDto);
        book.setCategories(categories);
        bookRepository.save(book);
        return bookMapper.toBookDto(book);
    }

    @Transactional
    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toBookDto);
    }

    @Transactional
    @Override
    public BookDto findById(long id) {
        return bookMapper.toBookDto(findBookById(id));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto updateById(Long id, CreateBookRequestDto createBookRequestDto) {
        Book book = findBookById(id);
        bookMapper.updateBookFromDto(createBookRequestDto, book);
        bookRepository.save(book);
        return bookMapper.toBookDto(book);
    }

    @Override
    public List<BookDto> search(BookSearchParameters params) {
        Specification<Book> specification = specificationBuilder.build(params);
        return bookRepository.findAll(specification)
                .stream()
                .map(bookMapper::toBookDto)
                .toList();
    }

    private Book findBookById(long id) {
        return bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Book not found with id " + id));
    }

    @Override
    public List<BookDtoWithoutCategoryIds> getAllByCategoryId(Long categoryId, Pageable pageable) {
        return bookRepository.findAllByCategoryId(categoryId, pageable).stream()
                .map(bookMapper::toDtoWithoutCategoryIds)
                .toList();
    }
}
