package com.study.commerce_lab.application.provided;

import com.study.commerce_lab.application.required.ProductRepository;
import com.study.commerce_lab.domain.product.Product;
import com.study.commerce_lab.domain.product.ProductRegisterRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductFinderTest {

    @Autowired
    ProductFinder productFinder;

    @Autowired
    ProductRepository productRepository;

    @Test
    void findById(){
        Product product = Product.create(new ProductRegisterRequest("APPLE-001", "아이폰 17", 1200000, 100));

        productRepository.save(product);

        Product found = productFinder.findProductById(product.getId());

        assertThat(found.getId()).isEqualTo(product.getId());
    }
}