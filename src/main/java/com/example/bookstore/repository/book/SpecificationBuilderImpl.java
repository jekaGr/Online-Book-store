package com.example.bookstore.repository.book;

import com.example.bookstore.dto.book.BookSearchParameters;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.book.specification.AuthorSpecificationProvider;
import com.example.bookstore.repository.book.specification.PriceSpecification;
import com.example.bookstore.repository.book.specification.TittlePartSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpecificationBuilderImpl implements SpecificationBuilder<Book> {
    private SpecificationProviderManager<Book> specificationProviders;

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> spec = Specification.where(null);
        if (searchParameters.prices() != null && searchParameters.prices().length > 0) {
            spec = spec.and(specificationProviders.getSpecificationProvider(
                    PriceSpecification.PRICE).getSpecification(searchParameters.prices()));
        }
        if (searchParameters.author() != null && searchParameters.author().length > 0) {
            spec = spec.and(specificationProviders.getSpecificationProvider(
                    AuthorSpecificationProvider.AUTHOR)
                    .getSpecification(searchParameters.author()));
        }
        if (searchParameters.titlePart() != null && searchParameters.titlePart().length > 0) {
            spec = spec.and(specificationProviders.getSpecificationProvider(
                    TittlePartSpecification.TITLE).getSpecification(searchParameters.titlePart()));
        }
        return spec;
    }
}
