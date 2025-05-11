package com.example.brokagefrim.exception;

public class OrderCannotBeCanceledException extends RuntimeException{
    public OrderCannotBeCanceledException(String message) {
        super(message);
    }
}
