package com.example.bookstore.repository.book;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {

    String getKey();

    public Specification<T> getSpecification(String[] params);
}
