package com.study.commerce_lab.domain.product;

import com.study.commerce_lab.adapter.in.webapi.request.ProductRegisterRequest;
import com.study.commerce_lab.domain.product.exception.InsufficientStockException;
import com.study.commerce_lab.domain.product.exception.StockQuantityExceededException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("재고 100개에 30개를 입고하면 130개가 된다")
    void increaseStockQuantity() {
        product.increaseStockQuantity(30);

        assertThat(product.getStockQuantity()).isEqualTo(130);

    }

    @Test
    @DisplayName("재고 100개에서 40개를 차감하면 60개가 된다")
    void decreaseStockQuantity() {
        product.decreaseStockQuantity(40);

        assertThat(product.getStockQuantity()).isEqualTo(60);
    }

    @Test
    @DisplayName("최대 재고 1,000,000개까지 입고할 수 있다")
    void increaseStockToMaximum() {
        product.increaseStockQuantity(999_900);
        assertThat(product.getStockQuantity()).isEqualTo(1_000_000);
    }

    @Test
    @DisplayName("최대 재고를 초과하는 입고는 실패하고 기존 재고를 유지한다")
    void preserveStockWhenIncreaseExceedsMaximum() {
        Product fullStock = productWithStock(1_000_000);
        assertThatThrownBy(() -> fullStock.increaseStockQuantity(1))
                .isInstanceOf(StockQuantityExceededException.class);
        assertThat(fullStock.getStockQuantity()).isEqualTo(1_000_000);
    }

    @Test
    @DisplayName("재고보다 많은 수량의 차감은 실패하고 기존 재고를 유지한다")
    void preserveStockWhenStockIsInsufficient() {
        Product lowStock = productWithStock(10);
        assertThatThrownBy(() -> lowStock.decreaseStockQuantity(11))
                .isInstanceOf(InsufficientStockException.class);
        assertThat(lowStock.getStockQuantity()).isEqualTo(10);
    }

    @Test
    @DisplayName("재고 전량을 차감하면 재고가 0개가 된다")
    void decreaseEntireStockToZero() {
        Product lowStock = productWithStock(10);
        lowStock.decreaseStockQuantity(10);
        assertThat(lowStock.getStockQuantity()).isZero();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 1_000_001, Integer.MAX_VALUE})
    @DisplayName("유효하지 않은 입고 수량은 거부하고 기존 재고를 유지한다")
    void rejectInvalidIncreaseQuantityAndPreserveStock(int quantity) {
        assertThatThrownBy(() -> product.increaseStockQuantity(quantity))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(product.getStockQuantity()).isEqualTo(100);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 1_000_001, Integer.MAX_VALUE})
    @DisplayName("유효하지 않은 차감 수량은 거부하고 기존 재고를 유지한다")
    void rejectInvalidDecreaseQuantityAndPreserveStock(int quantity) {
        assertThatThrownBy(() -> product.decreaseStockQuantity(quantity))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(product.getStockQuantity()).isEqualTo(100);
    }

    private Product productWithStock(int stock) {
        return Product.create(new ProductRegisterRequest("APPLE-1", "아이폰 17", 1200000, stock));
    }
}
