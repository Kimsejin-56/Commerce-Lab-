package com.study.commerce_lab.adapter.in.webapi;

import com.study.commerce_lab.application.port.in.ProductFinder;
import com.study.commerce_lab.application.port.in.ProductRegister;
import com.study.commerce_lab.application.port.in.ProductStockManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ProductRegister productRegister;

    @MockitoBean
    ProductStockManager  productStockManager;

    @MockitoBean
    ProductFinder productFinder;

    @Test
    @DisplayName("입고 수량이 0이면 quantity 필드 오류와 400을 반환한다")
    void returnValidationErrorForZeroIncreaseQuantity() throws Exception {
        mockMvc.perform(post("/api/products/1/stock/increase")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "quantity": 0
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("INVALID_INPUT"))
                .andExpect(jsonPath("$.errors[0].field").value("quantity"));
    }

    @Test
    @DisplayName("입고 수량이 상한을 초과하면 quantity 필드 오류와 400을 반환한다")
    void returnValidationErrorForIncreaseQuantityAboveMaximum() throws Exception {
        mockMvc.perform(post("/api/products/1/stock/increase")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "quantity": 1000001
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("INVALID_INPUT"))
                .andExpect(jsonPath("$.errors[0].field").value("quantity"));
    }

    static Stream<Arguments> missingQuantities() {
        return Stream.of("increase", "decrease").flatMap(direction ->
                Stream.of("{}", "{\"quantity\":null}")
                        .map(body -> Arguments.of(direction, body)));
    }

    @ParameterizedTest(name = "{0}: {1}")
    @MethodSource("missingQuantities")
    @DisplayName("입고·차감 수량이 누락되거나 null이면 필수값 검증 오류를 반환한다")
    void rejectMissingQuantity(String direction, String body) throws Exception {
        mockMvc.perform(post("/api/products/1/stock/" + direction)
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("INVALID_INPUT"))
                .andExpect(jsonPath("$.errors[0].field").value("quantity"));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1_000_001})
    @DisplayName("차감 요청에도 수량 범위 검증이 적용된다")
    void rejectOutOfRangeDecreaseQuantity(int quantity) throws Exception {
        mockMvc.perform(post("/api/products/1/stock/decrease")
                        .contentType(APPLICATION_JSON)
                        .content("{\"quantity\":" + quantity + "}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("INVALID_INPUT"))
                .andExpect(jsonPath("$.errors[0].field").value("quantity"));
    }
}
