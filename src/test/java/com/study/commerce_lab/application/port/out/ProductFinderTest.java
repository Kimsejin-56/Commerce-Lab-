package com.study.commerce_lab.application.port.out;

import com.study.commerce_lab.domain.product.request.ProductRegisterRequest;
import com.study.commerce_lab.application.port.in.ProductFinder;
import com.study.commerce_lab.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class ProductFinderTest {

    @Autowired
    ProductFinder productFinder;

    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("저장한 상품을 ID로 조회할 수 있다")
    void findById(){
        Product product = Product.create(new ProductRegisterRequest("APPLE-1", "아이폰 17", 1200000, 100));

        productRepository.save(product);

        Product found = productFinder.findProductById(product.getId());

        assertThat(found.getId()).isEqualTo(product.getId());
    }
}
