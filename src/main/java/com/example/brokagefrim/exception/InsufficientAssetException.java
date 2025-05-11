package com.example.brokagefrim.exception;

public class InsufficientAssetException extends RuntimeException {
    public InsufficientAssetException(String message) {
        super(message);
    }
}