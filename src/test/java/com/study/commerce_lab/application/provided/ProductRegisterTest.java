package com.study.commerce_lab.application.provided;

import com.study.commerce_lab.adapter.webapi.ProductRegisterResponse;
import com.study.commerce_lab.application.required.ProductRepository;
import com.study.commerce_lab.domain.product.DuplicateSkuException;
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
        ProductRegisterResponse response = productRegister.registerProduct(new ProductRegisterRequest("APPLE-001", "아이폰 17", 1200000, 100));

        Assertions.assertThat(response.getId()).isNotNull();
    }

    @Test
    void duplicateSkuFail(){
        productRegister.registerProduct(new ProductRegisterRequest("APPLE-001", "아이폰 17", 1200000, 100));

        Assertions.assertThatThrownBy(() -> productRegister.registerProduct(new ProductRegisterRequest("APPLE-001", "아이폰 17", 1200000, 100)))
            .isInstanceOf(DuplicateSkuException.class);
    }
}