package com.study.commerce_lab.application.provided;

import com.study.commerce_lab.adapter.in.webapi.reponse.IncreaseStockQuantityResponse;
import com.study.commerce_lab.adapter.in.webapi.request.IncreaseStockQuantityRequest;
import com.study.commerce_lab.application.port.in.ProductRegister;
import com.study.commerce_lab.domain.product.Product;
import com.study.commerce_lab.domain.product.exception.DuplicateSkuException;
import com.study.commerce_lab.adapter.in.webapi.request.ProductRegisterRequest;
import com.study.commerce_lab.domain.product.exception.StockQuantityExceededException;
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

    @Test
    void registerProduct(){
        Product response = productRegister.registerProduct(new ProductRegisterRequest("APPLE-001", "아이폰 17", 1200000, 100));

        assertThat(response.getId()).isNotNull();
    }

    @Test
    void duplicateSkuFail(){
        productRegister.registerProduct(new ProductRegisterRequest("APPLE-001", "아이폰 17", 1200000, 100));

        assertThatThrownBy(() -> productRegister.registerProduct(new ProductRegisterRequest("APPLE-001", "아이폰 17", 1200000, 100)))
            .isInstanceOf(DuplicateSkuException.class);
    }

    @Test
    void increaseStockQuantity(){
        Product product = productRegister.registerProduct(
                new ProductRegisterRequest("APPLE-002", "아이폰 17", 1200000, 100));

        IncreaseStockQuantityResponse response =
                productRegister.increaseStockQuantity(product.getId(), new IncreaseStockQuantityRequest(30));

        assertThat(response).isNotNull();
        assertThat(response.stockQuantity()).isEqualTo(130);
    }

    @Test
    void increaseStockQuantityFail(){
        Product product = productRegister.registerProduct(
                new ProductRegisterRequest("APPLE-002", "아이폰 17", 1200000, 100));

        assertThatThrownBy(() -> productRegister.increaseStockQuantity(product.getId(), new IncreaseStockQuantityRequest(999999)))
            .isInstanceOf(StockQuantityExceededException.class);
    }
}
