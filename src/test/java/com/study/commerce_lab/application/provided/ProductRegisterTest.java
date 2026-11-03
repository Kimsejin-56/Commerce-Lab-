package com.study.commerce_lab.application.provided;

import com.study.commerce_lab.adapter.in.webapi.reponse.AddStockQuantityResponse;
import com.study.commerce_lab.adapter.in.webapi.request.AddStockQuantityRequest;
import com.study.commerce_lab.application.port.in.ProductRegister;
import com.study.commerce_lab.domain.product.Product;
import com.study.commerce_lab.domain.product.exception.DuplicateSkuException;
import com.study.commerce_lab.adapter.in.webapi.request.ProductRegisterRequest;
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
    void addStockQuantity(){
        Product product = productRegister.registerProduct(
                new ProductRegisterRequest("APPLE-001", "아이폰 17", 1200000, 100));

        AddStockQuantityResponse response =
                productRegister.addStockQuantity(product.getId(), new AddStockQuantityRequest(30));

        assertThat(response).isNotNull();
        assertThat(response.stockQuantity()).isEqualTo(130);
    }
}
