package com.example.bookstore.repository.book;

import com.example.bookstore.exception.DataProcessingException;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.SpecificationProvider;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PriceSpecification implements SpecificationProvider<Book> {
    public static final String PRICE = "price";

    @Override
    public String getKey() {
        return PRICE;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params != null && params.length > 0) {
                try {
                    Optional.ofNullable(params[0])
                            .filter(price -> !price.isEmpty())
                            .map(BigDecimal::new)
                            .ifPresent(minPrice ->
                                    predicates.add(criteriaBuilder
                                            .greaterThanOrEqualTo(root.get(PRICE), minPrice))
                            );

                    Optional.ofNullable(params.length > 1 ? params[1] : null)
                            .filter(price -> !price.isEmpty())
                            .map(BigDecimal::new)
                            .ifPresent(maxPrice ->
                                    predicates.add(criteriaBuilder
                                            .lessThanOrEqualTo(root.get(PRICE), maxPrice))
                            );

                } catch (NumberFormatException e) {
                    throw new DataProcessingException("Could not parse price", e);
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
