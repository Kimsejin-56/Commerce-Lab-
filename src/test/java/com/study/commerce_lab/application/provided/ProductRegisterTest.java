package com.study.commerce_lab.application.provided;

import com.study.commerce_lab.adapter.in.webapi.reponse.DecreaseStockQuantityResponse;
import com.study.commerce_lab.adapter.in.webapi.reponse.IncreaseStockQuantityResponse;
import com.study.commerce_lab.adapter.in.webapi.request.DecreaseStockQuantityRequest;
import com.study.commerce_lab.adapter.in.webapi.request.IncreaseStockQuantityRequest;
import com.study.commerce_lab.application.port.in.ProductFinder;
import com.study.commerce_lab.application.port.in.ProductRegister;
import com.study.commerce_lab.domain.product.Product;
import com.study.commerce_lab.domain.product.exception.DuplicateSkuException;
import com.study.commerce_lab.adapter.in.webapi.request.ProductRegisterRequest;
import com.study.commerce_lab.domain.product.exception.InsufficientStockException;
import com.study.commerce_lab.domain.product.exception.StockQuantityExceededException;
import com.study.commerce_lab.domain.product.exception.ProductNotFoundException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class ProductRegisterTest {

    @Autowired
    ProductRegister productRegister;

    @Autowired
    ProductFinder productFinder;

    @Autowired
    EntityManager entityManager;

    @Autowired
    MockMvc mockMvc;

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

    @Test
    @DisplayName("입고 결과와 DB에 저장된 재고가 130개로 일치한다")
    void increaseStockQuantity(){
        Product product = productRegister.registerProduct(
                new ProductRegisterRequest("APPLE-002", "아이폰 17", 1200000, 100));

        IncreaseStockQuantityResponse response =
                productRegister.increaseStockQuantity(product.getId(), new IncreaseStockQuantityRequest(30));

        assertThat(response).isNotNull();
        assertThat(response.stockQuantity()).isEqualTo(130);

        // 변경 내용을 SQL로 반영하고 1차 캐시를 비운 뒤 DB에서 다시 읽는다.
        entityManager.flush();
        entityManager.clear();
        assertThat(productFinder.findProductById(product.getId()).getStockQuantity()).isEqualTo(130);
    }

    @Test
    @DisplayName("최대 재고를 초과하는 입고가 실패하면 객체의 기존 재고를 유지한다")
    void increaseStockQuantityFail(){
        Product product = productRegister.registerProduct(
                new ProductRegisterRequest("APPLE-002", "아이폰 17", 1200000, 100));

        assertThatThrownBy(() -> productRegister.increaseStockQuantity(product.getId(), new IncreaseStockQuantityRequest(999999)))
            .isInstanceOf(StockQuantityExceededException.class);

        assertThat(product.getStockQuantity()).isEqualTo(100);
    }

    @Test
    @DisplayName("차감 결과와 DB에 저장된 재고가 70개로 일치한다")
    void decreaseStockQuantity(){
        Product product = productRegister.registerProduct(
                new ProductRegisterRequest("APPLE-002", "아이폰 17", 1200000, 100));

        DecreaseStockQuantityResponse response =
                productRegister.decreaseStockQuantity(product.getId(), new DecreaseStockQuantityRequest(30));

        assertThat(response).isNotNull();
        assertThat(response.stockQuantity()).isEqualTo(70);

        entityManager.flush();
        entityManager.clear();
        assertThat(productFinder.findProductById(product.getId()).getStockQuantity()).isEqualTo(70);
    }

    @Test
    @DisplayName("재고 부족으로 차감이 실패하면 객체의 기존 재고를 유지한다")
    void decreaseStockQuantityFail(){
        Product product = productRegister.registerProduct(
                new ProductRegisterRequest("APPLE-002", "아이폰 17", 1200000, 1));

        assertThatThrownBy(() -> productRegister.decreaseStockQuantity(product.getId(), new DecreaseStockQuantityRequest(30)))
            .isInstanceOf(InsufficientStockException.class);

        assertThat(product.getStockQuantity()).isEqualTo(1);
    }

    @Test
    @DisplayName("존재하지 않는 상품을 입고하면 상품 조회 예외가 발생한다")
    void rejectStockIncreaseForMissingProduct() {
        assertThatThrownBy(() -> productRegister.increaseStockQuantity(
                Long.MAX_VALUE, new IncreaseStockQuantityRequest(1)))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("존재하지 않는 상품을 차감하면 상품 조회 예외가 발생한다")
    void rejectStockDecreaseForMissingProduct() {
        assertThatThrownBy(() -> productRegister.decreaseStockQuantity(
                Long.MAX_VALUE, new DecreaseStockQuantityRequest(1)))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("입고 후 상품 조회 API는 DB에 반영된 재고 130개를 반환한다")
    void findProductReturnsIncreasedStock() throws Exception {
        Product product = productRegister.registerProduct(
                new ProductRegisterRequest("API-IN-001", "입고 상품", 1200000, 100));
        productRegister.increaseStockQuantity(product.getId(), new IncreaseStockQuantityRequest(30));
        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(get("/api/products/{id}", product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.stockQuantity").value(130));
    }

    @Test
    @DisplayName("차감 후 상품 조회 API는 DB에 반영된 재고 60개를 반환한다")
    void findProductReturnsDecreasedStock() throws Exception {
        Product product = productRegister.registerProduct(
                new ProductRegisterRequest("API-OUT-001", "차감 상품", 1200000, 100));
        productRegister.decreaseStockQuantity(product.getId(), new DecreaseStockQuantityRequest(40));
        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(get("/api/products/{id}", product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.stockQuantity").value(60));
    }
}
