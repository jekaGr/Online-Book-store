package com.example.bookstore.dto.book;

public record BookSearchParameters(String[] titlePart, String[] author,String[] prices) {
}
