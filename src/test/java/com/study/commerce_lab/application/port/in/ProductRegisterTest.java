package com.study.commerce_lab.application.port.in;

import com.study.commerce_lab.domain.product.request.ProductRegisterRequest;
import com.study.commerce_lab.domain.product.Product;
import com.study.commerce_lab.domain.product.exception.DuplicateSkuException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class ProductRegisterTest {

    @Autowired
    ProductRegister productRegister;

    @Autowired
    ProductFinder productFinder;

    @Autowired
    ProductStockManager productStockManager;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("상품을 등록하면 ID가 부여된다")
    void registerProduct(){
        Product response = productRegister.registerProduct(new ProductRegisterRequest("APPLE-1", "아이폰 17", 1200000, 100));

        assertThat(response.getId()).isNotNull();
    }

    @Test
    @DisplayName("이미 등록된 SKU로 상품을 등록하면 실패한다")
    void duplicateSkuFail(){
        productRegister.registerProduct(new ProductRegisterRequest("APPLE-1", "아이폰 17", 1200000, 100));

        assertThatThrownBy(() -> productRegister.registerProduct(new ProductRegisterRequest("APPLE-1", "아이폰 17", 1200000, 100)))
            .isInstanceOf(DuplicateSkuException.class);
    }


}
