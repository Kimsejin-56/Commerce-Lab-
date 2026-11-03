package com.study.commerce_lab.domain.product.exception;

public class StockQuantityExceededException extends RuntimeException {
    public StockQuantityExceededException(String message) {
        super(message);
    }
}
