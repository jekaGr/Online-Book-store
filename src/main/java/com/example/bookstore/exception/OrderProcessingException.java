package com.example.bookstore.exception;

public class OrderProcessingException extends RuntimeException {
    public OrderProcessingException(String string) {
        super(string);
    }
}
