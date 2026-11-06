package com.study.commerce_lab.domain.product;

import com.study.commerce_lab.domain.product.request.ProductRegisterRequest;
import com.study.commerce_lab.domain.product.exception.InsufficientStockException;
import com.study.commerce_lab.domain.product.exception.StockQuantityExceededException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {
    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.create(new ProductRegisterRequest("APPLE-1", "아이폰 17", 1200000, 100));
    }

    @Test
    @DisplayName("상품 생성 시 입력한 속성이 유지된다")
    void create() {
        assertThat(product).isNotNull();
        assertThat(product.getSku()).isEqualTo("APPLE-1");
        assertThat(product.getPrice()).isEqualTo(1200000);
        assertThat(product.getName()).isEqualTo("아이폰 17");
        assertThat(product.getStockQuantity()).isEqualTo(100);
    }

    @Test
    @DisplayName("입고에 성공하고 최대 재고 초과 시 기존 재고를 유지한다")
    void increaseStockQuantity() {
        product.increaseStockQuantity(30);

        assertThat(product.getStockQuantity()).isEqualTo(130);

        assertThatThrownBy(() -> product.increaseStockQuantity(999999))
            .isInstanceOf(StockQuantityExceededException.class);
        assertThat(product.getStockQuantity()).isEqualTo(130);
    }

    @Test
    @DisplayName("차감에 성공하고 재고 부족 시 기존 재고를 유지한다")
    void decreaseStockQuantity() {
        product.decreaseStockQuantity(30);

        assertThat(product.getStockQuantity()).isEqualTo(70);

        assertThatThrownBy(() -> product.decreaseStockQuantity(999999))
            .isInstanceOf(InsufficientStockException.class);
        assertThat(product.getStockQuantity()).isEqualTo(70);
    }

    @Test
    @DisplayName("최대 재고까지 입고할 수 있고 1개를 더 입고하면 실패한다")
    void increaseStockToMaximum() {
        product.increaseStockQuantity(999_900);
        assertThat(product.getStockQuantity()).isEqualTo(1_000_000);

        assertThatThrownBy(() -> product.increaseStockQuantity(1))
                .isInstanceOf(StockQuantityExceededException.class);
        assertThat(product.getStockQuantity()).isEqualTo(1_000_000);
    }

    @Test
    @DisplayName("재고 전량을 차감하면 0개가 된다")
    void decreaseEntireStockToZero() {
        product.decreaseStockQuantity(100);
        assertThat(product.getStockQuantity()).isZero();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1_000_001})
    @DisplayName("입고 수량 자체가 범위를 벗어나면 예외가 발생하고 기존 재고를 유지한다")
    void rejectInvalidIncreaseQuantity(int quantity) {
        assertThatThrownBy(() -> product.increaseStockQuantity(quantity))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(product.getStockQuantity()).isEqualTo(100);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 1_000_001})
    @DisplayName("차감 수량 자체가 범위를 벗어나면 예외가 발생하고 기존 재고를 유지한다")
    void rejectInvalidDecreaseQuantity(int quantity) {
        assertThatThrownBy(() -> product.decreaseStockQuantity(quantity))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(product.getStockQuantity()).isEqualTo(100);
    }
}
