package com.study.commerce_lab.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void create() {
        Product product =
                Product.create(new ProductRegisterRequest("APPLE-001", "아이폰 17", 1200000, 100));

        assertThat(product).isNotNull();
        assertThat(product.getSku()).isEqualTo("APPLE-001");
        assertThat(product.getPrice()).isEqualTo(1200000);
        assertThat(product.getName()).isEqualTo("아이폰 17");
        assertThat(product.getStockQuantity()).isEqualTo(100);
    }
}