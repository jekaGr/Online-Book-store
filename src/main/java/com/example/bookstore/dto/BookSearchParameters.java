package com.example.bookstore.dto;

public record BookSearchParameters(String[] titlePart, String[] author,String[] prices) {
}

