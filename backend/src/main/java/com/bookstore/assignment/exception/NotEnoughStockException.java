package com.bookstore.assignment.exception;

public class NotEnoughStockException extends RuntimeException {

    public NotEnoughStockException(String message) {
        super(message);
    }
}
