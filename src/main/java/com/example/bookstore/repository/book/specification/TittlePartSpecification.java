package com.example.bookstore.repository.book.specification;

import com.example.bookstore.model.Book;
import com.example.bookstore.repository.book.SpecificationProvider;
import jakarta.persistence.criteria.Predicate;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TittlePartSpecification implements SpecificationProvider<Book> {
    public static final String TITLE = "title";

    @Override
    public String getKey() {
        return TITLE;
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.or(
                        Arrays.stream(params).map(param ->
                                        criteriaBuilder.like(root.get(TITLE), "%" + param + "%"))
                                .toArray(Predicate[]::new)
                );
    }
}
