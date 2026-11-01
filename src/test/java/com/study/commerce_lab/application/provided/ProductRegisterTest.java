package com.study.commerce_lab.application.provided;

import com.study.commerce_lab.adapter.webapi.ProductRegisterResponse;
import com.study.commerce_lab.domain.product.Product;
import com.study.commerce_lab.domain.product.ProductRegisterRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ProductRegisterTest {

    @Autowired
    ProductRegister productRegister;

    @Test
    void registerProduct(){
        ProductRegisterRequest request = new ProductRegisterRequest("APPLE-001", "아이폰 17", 1200000, 100);

        ProductRegisterResponse response = productRegister.registerProduct(request);

        Assertions.assertThat(response.getId()).isNotNull();
    }
}