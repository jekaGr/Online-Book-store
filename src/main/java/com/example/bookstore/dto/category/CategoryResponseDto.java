package com.example.bookstore.dto.category;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
public class CategoryResponseDto {
    private Long id;
    private String name;
    private String description;
}
