package com.example.bookstore;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.example.bookstore.dto.category.CategoryRequestDto;
import com.example.bookstore.dto.category.CategoryResponseDto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestUtil {
    public static CreateBookRequestDto createBookRequestDto() {
        return new CreateBookRequestDto()
                .setAuthor("Test Author3")
                .setPrice(BigDecimal.valueOf(12.99))
                .setTitle("Test Book3")
                .setIsbn("12324564897")
                .setDescription("Test Description3")
                .setCategoryIds(List.of(1L));
    }

    public static BookDto getBookDto() {
        return new BookDto().setId(1L)
                .setAuthor("Test author")
                .setTitle("Test book")
                .setPrice(BigDecimal.valueOf(40.99))
                .setDescription("Test description")
                .setIsbn("123456789")
                .setCategoryIds(List.of(1L));
    }

    public static List<BookDto> createBookDtoList() {
        List<BookDto> bookDtoList = new ArrayList<>();
        BookDto book1 = new BookDto().setId(1L)
                .setAuthor("Test author")
                .setTitle("Test book")
                .setPrice(BigDecimal.valueOf(40.99))
                .setDescription("Test description")
                .setIsbn("123456789")
                .setCategoryIds(List.of(1L));
        bookDtoList.add(book1);

        BookDto book2 = new BookDto().setId(2L)
                .setAuthor("Test author1")
                .setTitle("Test book1")
                .setPrice(BigDecimal.valueOf(30.99))
                .setDescription("Test description1")
                .setIsbn("123456798")
                .setCategoryIds(List.of(2L));
        bookDtoList.add(book2);

        BookDto book3 = new BookDto().setId(3L)
                .setAuthor("Test author2")
                .setTitle("Test book2")
                .setPrice(BigDecimal.valueOf(10.99))
                .setDescription("Test description2")
                .setIsbn("123456987")
                .setCategoryIds(List.of(3L));
        bookDtoList.add(book3);

        return bookDtoList;
    }

    public static CategoryRequestDto createCategoryRequestDto() {
        return new CategoryRequestDto().setName("category4")
                .setDescription("description4");
    }

    public static CategoryResponseDto createCategoryResponseDto() {
        return new CategoryResponseDto().setId(4L)
                .setName("category4").setDescription("description4");
    }

    public static CategoryResponseDto getCategoryResponseDto() {
        return new CategoryResponseDto().setId(1L).setName("category1")
                .setDescription("description1");
    }

    public static List<CategoryResponseDto> getCategoryResponseDtoList() {
        List<CategoryResponseDto> categoryResponseDtoList = new ArrayList<>();
        categoryResponseDtoList.add(new CategoryResponseDto().setId(1L)
                .setName("category1")
                .setDescription("category desc1"));
        categoryResponseDtoList.add(new CategoryResponseDto().setId(2L)
                .setName("category2")
                .setDescription("category desc2"));
        categoryResponseDtoList.add(new CategoryResponseDto().setId(3L)
                .setName("category3")
                .setDescription("category desc3"));
        return categoryResponseDtoList;
    }
}
