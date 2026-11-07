package com.study.commerce_lab.domain.order.exception;

public class DuplicateOrderProductException extends RuntimeException {
    public DuplicateOrderProductException(Long productId) {
        super("주문에 동일한 상품을 중복해서 담을 수 없습니다. productId=" + productId);
    }
}
