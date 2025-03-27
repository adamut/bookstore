package com.bookstore.assignment.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String message) {
        super(message);
    }
}
