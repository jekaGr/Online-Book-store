package com.example.bookstore;

import com.example.bookstore.model.Book;
import com.example.bookstore.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookStoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book1 = new Book();
            book1.setTitle("Book 1");
            book1.setAuthor("Author 1");
            book1.setIsbn("ISBN 1");
            book1.setPrice(BigDecimal.valueOf(100.00));
            bookService.save(book1);
            Book book2 = new Book();
            book2.setTitle("Book 2");
            book2.setAuthor("Author 2");
            book2.setIsbn("ISBN 2");
            book2.setPrice(BigDecimal.valueOf(200.00));
            bookService.save(book2);

            System.out.println(bookService.findAll());
        };
    }

}
