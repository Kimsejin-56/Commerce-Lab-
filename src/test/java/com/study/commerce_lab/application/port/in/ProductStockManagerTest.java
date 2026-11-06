package com.study.commerce_lab.application.port.in;

import com.study.commerce_lab.domain.product.request.DecreaseStockQuantityRequest;
import com.study.commerce_lab.domain.product.request.IncreaseStockQuantityRequest;
import com.study.commerce_lab.domain.product.request.ProductRegisterRequest;
import com.study.commerce_lab.domain.product.Product;
import com.study.commerce_lab.domain.product.exception.InsufficientStockException;
import com.study.commerce_lab.domain.product.exception.ProductNotFoundException;
import com.study.commerce_lab.domain.product.exception.StockQuantityExceededException;
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
class ProductStockManagerTest {
    @Autowired
    ProductStockManager productStockManager;

    @Autowired
    ProductRegister  productRegister;

    @Autowired
    EntityManager entityManager;

    @Autowired
    ProductFinder productFinder;

    @Test
    @DisplayName("입고 결과가 130개이고 DB에도 같은 값이 반영된다")
    void increaseStockQuantity(){
        Product product = productRegister.registerProduct(
                new ProductRegisterRequest("APPLE-002", "아이폰 17", 1200000, 100));

        Product response =
                productStockManager.increaseStockQuantity(product.getId(), new IncreaseStockQuantityRequest(30));

        assertThat(response).isNotNull();
        assertThat(response.getStockQuantity()).isEqualTo(130);

        // 1차 캐시를 비운 뒤 DB에 반영된 값을 확인한다.
        entityManager.flush();
        entityManager.clear();
        assertThat(productFinder.findProductById(product.getId()).getStockQuantity()).isEqualTo(130);
    }

    @Test
    @DisplayName("최대 재고 초과로 입고가 실패하면 기존 재고 100개를 유지한다")
    void increaseStockQuantityFail(){
        Product product = productRegister.registerProduct(
                new ProductRegisterRequest("APPLE-002", "아이폰 17", 1200000, 100));

        assertThatThrownBy(() -> productStockManager.increaseStockQuantity(product.getId(), new IncreaseStockQuantityRequest(999999)))
                .isInstanceOf(StockQuantityExceededException.class);
        assertThat(product.getStockQuantity()).isEqualTo(100);
    }

    @Test
    @DisplayName("차감 결과가 70개이고 DB에도 같은 값이 반영된다")
    void decreaseStockQuantity(){
        Product product = productRegister.registerProduct(
                new ProductRegisterRequest("APPLE-002", "아이폰 17", 1200000, 100));

        Product response =
                productStockManager.decreaseStockQuantity(product.getId(), new DecreaseStockQuantityRequest(30));

        assertThat(response).isNotNull();
        assertThat(response.getStockQuantity()).isEqualTo(70);

        // 1차 캐시를 비운 뒤 DB에 반영된 값을 확인한다.
        entityManager.flush();
        entityManager.clear();
        assertThat(productFinder.findProductById(product.getId()).getStockQuantity()).isEqualTo(70);
    }

    @Test
    @DisplayName("재고 부족으로 차감이 실패하면 기존 재고 1개를 유지한다")
    void decreaseStockQuantityFail(){
        Product product = productRegister.registerProduct(
                new ProductRegisterRequest("APPLE-002", "아이폰 17", 1200000, 1));

        assertThatThrownBy(() -> productStockManager.decreaseStockQuantity(product.getId(), new DecreaseStockQuantityRequest(30)))
                .isInstanceOf(InsufficientStockException.class);
        assertThat(product.getStockQuantity()).isEqualTo(1);
    }

    @Test
    @DisplayName("존재하지 않는 상품의 입고는 실패한다")
    void increaseStockForMissingProduct() {
        assertThatThrownBy(() -> productStockManager.increaseStockQuantity(
                Long.MAX_VALUE, new IncreaseStockQuantityRequest(1)))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("존재하지 않는 상품의 차감은 실패한다")
    void decreaseStockForMissingProduct() {
        assertThatThrownBy(() -> productStockManager.decreaseStockQuantity(
                Long.MAX_VALUE, new DecreaseStockQuantityRequest(1)))
                .isInstanceOf(ProductNotFoundException.class);
    }
}
