package com.study.commerce_lab.adapter.in.webapi;

import com.jayway.jsonpath.JsonPath;
import com.study.commerce_lab.application.port.in.ProductRegister;
import com.study.commerce_lab.domain.product.request.ProductRegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:order-api-test",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class OrderControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired ProductRegister productRegister;
    @Autowired JdbcTemplate jdbc;

    @Test
    @DisplayName("여러 상품을 주문하면 재고가 차감되고 단건 조회에서 항목 순서와 금액이 유지된다")
    void createsAndReadsMultiProductOrder() throws Exception {
        long first = register("ORDER-A", 1_000, 10);
        long second = register("ORDER-B", 2_000, 10);

        String response = mockMvc.perform(post("/api/orders").contentType(APPLICATION_JSON)
                        .content(orderBody(second, 3, first, 2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.totalAmount").value(8_000))
                .andExpect(jsonPath("$.items[0].productId").value(second))
                .andReturn().getResponse().getContentAsString();
        Number orderId = JsonPath.read(response, "$.orderId");

        mockMvc.perform(get("/api/orders/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmount").value(8_000))
                .andExpect(jsonPath("$.items.length()").value(2))
                .andExpect(jsonPath("$.items[0].productId").value(second))
                .andExpect(jsonPath("$.items[0].quantity").value(3))
                .andExpect(jsonPath("$.items[0].unitPrice").value(2_000))
                .andExpect(jsonPath("$.items[0].lineAmount").value(6_000))
                .andExpect(jsonPath("$.items[1].productId").value(first));
        assertThat(stock(first)).isEqualTo(8);
        assertThat(stock(second)).isEqualTo(7);
    }

    @Test
    @DisplayName("두 번째 상품의 재고가 부족하면 409를 반환하고 주문과 재고 변경을 롤백한다")
    void rollsBackWhenSecondProductHasInsufficientStock() throws Exception {
        long first = register("ROLLBACK-A", 1_000, 10);
        long second = register("ROLLBACK-B", 2_000, 1);
        long ordersBefore = jdbc.queryForObject("SELECT COUNT(*) FROM orders", Long.class);

        mockMvc.perform(post("/api/orders").contentType(APPLICATION_JSON)
                        .content(orderBody(first, 2, second, 2)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("INSUFFICIENT_STOCK_QUANTITY"));

        assertThat(stock(first)).isEqualTo(10);
        assertThat(stock(second)).isEqualTo(1);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM orders", Long.class)).isEqualTo(ordersBefore);
    }

    @Test
    @DisplayName("같은 상품 ID를 두 번 요청하면 400을 반환한다")
    void rejectsDuplicateProductIds() throws Exception {
        mockMvc.perform(post("/api/orders").contentType(APPLICATION_JSON)
                        .content(orderBody(1, 1, 1, 2)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("DUPLICATE_ORDER_PRODUCT"));
    }

    @Test
    @DisplayName("존재하지 않는 주문을 조회하면 404를 반환한다")
    void returnsNotFoundForMissingOrder() throws Exception {
        mockMvc.perform(get("/api/orders/{orderId}", Long.MAX_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("ORDER_NOT_FOUND"));
    }

    private long register(String sku, long price, int stock) {
        return productRegister.registerProduct(new ProductRegisterRequest(sku, "테스트 상품", price, stock)).getId();
    }

    private int stock(long productId) {
        return jdbc.queryForObject("SELECT stock_quantity FROM product WHERE id = ?", Integer.class, productId);
    }

    private String orderBody(long first, int firstQuantity, long second, int secondQuantity) {
        return """
                {"items":[{"productId":%d,"quantity":%d},{"productId":%d,"quantity":%d}]}
                """.formatted(first, firstQuantity, second, secondQuantity);
    }
}
