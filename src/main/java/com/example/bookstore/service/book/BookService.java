package com.example.bookstore.service.book;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.book.BookSearchParameters;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    Page<BookDto> findAll(Pageable pageable);

    BookDto findById(long id);

    void deleteById(Long id);

    BookDto updateById(Long id, CreateBookRequestDto createBookRequestDto);

    public List<BookDto> search(BookSearchParameters bookSearchParameters);

    List<BookDtoWithoutCategoryIds> getAllByCategoryId(Long categoryId, Pageable pageable);
}
