package com.example.bookstore.repository.book;

import com.example.bookstore.exception.DataProcessingException;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.SpecificationProvider;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PriceSpecification implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "price";
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params != null && params.length > 0) {
                try {
                    if (!params[0].isEmpty()) {
                        BigDecimal minPrice = new BigDecimal(params[0]);
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"),
                                minPrice));
                    }

                    if (params.length > 1 && !params[1].isEmpty()) {
                        BigDecimal maxPrice = new BigDecimal(params[1]);
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"),
                                maxPrice));
                    }
                } catch (NumberFormatException e) {
                    new DataProcessingException("Could not parse price", e);
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
