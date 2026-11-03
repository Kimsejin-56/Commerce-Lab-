package com.study.commerce_lab.domain.product;

import com.study.commerce_lab.adapter.in.webapi.request.ProductRegisterRequest;
import com.study.commerce_lab.domain.product.exception.InsufficientStockException;
import com.study.commerce_lab.domain.product.exception.StockQuantityExceededException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.LimitExceededException;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {
    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.create(new ProductRegisterRequest("APPLE-1", "아이폰 17", 1200000, 100));
    }

    @Test
    void create() {
        assertThat(product).isNotNull();
        assertThat(product.getSku()).isEqualTo("APPLE-1");
        assertThat(product.getPrice()).isEqualTo(1200000);
        assertThat(product.getName()).isEqualTo("아이폰 17");
        assertThat(product.getStockQuantity()).isEqualTo(100);
    }

    @Test
    void increaseStockQuantity() {
        product.increaseStockQuantity(30);

        assertThat(product.getStockQuantity()).isEqualTo(130);

        Assertions.assertThatThrownBy(() -> product.increaseStockQuantity(999999))
            .isInstanceOf(StockQuantityExceededException.class);
    }

    @Test
    void decreaseStockQuantity() {
        product.decreaseStockQuantity(30);

        assertThat(product.getStockQuantity()).isEqualTo(70);

        Assertions.assertThatThrownBy(() -> product.decreaseStockQuantity(999999))
            .isInstanceOf(InsufficientStockException.class);
    }
}
