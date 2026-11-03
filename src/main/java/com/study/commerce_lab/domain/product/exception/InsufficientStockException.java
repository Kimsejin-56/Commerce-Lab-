package com.study.commerce_lab.domain.product.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String messge) {
        super(messge);
    }
}
