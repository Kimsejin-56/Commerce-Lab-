package com.study.commerce_lab.adapter.in.webapi;

import com.study.commerce_lab.application.port.in.ProductFinder;
import com.study.commerce_lab.application.port.in.ProductRegister;
import com.study.commerce_lab.adapter.in.webapi.request.IncreaseStockQuantityRequest;
import com.study.commerce_lab.adapter.in.webapi.request.DecreaseStockQuantityRequest;
import com.study.commerce_lab.adapter.in.webapi.reponse.IncreaseStockQuantityResponse;
import com.study.commerce_lab.adapter.in.webapi.reponse.DecreaseStockQuantityResponse;
import com.study.commerce_lab.domain.product.exception.ProductNotFoundException;
import com.study.commerce_lab.domain.product.exception.StockQuantityExceededException;
import com.study.commerce_lab.domain.product.exception.InsufficientStockException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.Arguments;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ProductRegister productRegister;

    @MockitoBean
    ProductFinder productFinder;

    static Stream<Arguments> invalidRequests() {
        return Stream.of("increase", "decrease").flatMap(direction ->
                Stream.of("{}", "{\"quantity\":null}", "{\"quantity\":-1}")
                        .map(body -> Arguments.of(direction, body)));
    }

    @ParameterizedTest
    @MethodSource("invalidRequests")
    @DisplayName("입고·차감 수량의 누락·null·음수는 서비스 호출 전에 400으로 거부한다")
    void rejectMissingNullOrNegativeQuantityBeforeCallingService(String direction, String body) throws Exception {
        assertInvalidRequest(direction, body);
    }

    @ParameterizedTest
    @ValueSource(strings = {"{\"quantity\":0}", "{\"quantity\":1000001}"})
    @DisplayName("허용 범위를 벗어난 차감 수량은 공통 검증 오류와 400을 반환한다")
    void returnBadRequestForOutOfRangeDecreaseQuantity(String body) throws Exception {
        assertInvalidRequest("decrease", body);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 1_000_000})
    @DisplayName("유효한 입고 수량은 서비스에 전달하고 결과를 200으로 반환한다")
    void passValidIncreaseQuantityToService(int quantity) throws Exception {
        when(productRegister.increaseStockQuantity(1L, new IncreaseStockQuantityRequest(quantity)))
                .thenReturn(new IncreaseStockQuantityResponse(1L, quantity));

        mockMvc.perform(post("/api/products/1/stock/increase")
                        .contentType(APPLICATION_JSON)
                        .content("{\"quantity\":" + quantity + "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.stockQuantity").value(quantity));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 1_000_000})
    @DisplayName("유효한 차감 수량은 서비스에 전달하고 결과를 200으로 반환한다")
    void passValidDecreaseQuantityToService(int quantity) throws Exception {
        when(productRegister.decreaseStockQuantity(1L, new DecreaseStockQuantityRequest(quantity)))
                .thenReturn(new DecreaseStockQuantityResponse(1L, 0));

        mockMvc.perform(post("/api/products/1/stock/decrease")
                        .contentType(APPLICATION_JSON)
                        .content("{\"quantity\":" + quantity + "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.stockQuantity").value(0));
    }

    @ParameterizedTest
    @ValueSource(strings = {"increase", "decrease"})
    @DisplayName("입고·차감 시 상품이 없으면 공통 오류 코드와 404를 반환한다")
    void returnNotFoundForMissingProduct(String direction) throws Exception {
        if (direction.equals("increase")) {
            when(productRegister.increaseStockQuantity(1L, new IncreaseStockQuantityRequest(1)))
                    .thenThrow(new ProductNotFoundException(1L));
        } else {
            when(productRegister.decreaseStockQuantity(1L, new DecreaseStockQuantityRequest(1)))
                    .thenThrow(new ProductNotFoundException(1L));
        }
        mockMvc.perform(post("/api/products/1/stock/" + direction)
                        .contentType(APPLICATION_JSON).content("{\"quantity\":1}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("PRODUCT_NOT_FOUND"));
    }

    @Test
    @DisplayName("최대 재고 초과 예외는 공통 오류 코드와 409를 반환한다")
    void returnConflictWhenStockExceedsMaximum() throws Exception {
        when(productRegister.increaseStockQuantity(1L, new IncreaseStockQuantityRequest(1)))
                .thenThrow(new StockQuantityExceededException("재고 수량 초과입니다."));
        mockMvc.perform(post("/api/products/1/stock/increase")
                        .contentType(APPLICATION_JSON).content("{\"quantity\":1}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("EXCEED_STOCK_QUANTITY"));
    }

    @Test
    @DisplayName("재고 부족 예외는 공통 오류 코드와 409를 반환한다")
    void returnConflictWhenStockIsInsufficient() throws Exception {
        when(productRegister.decreaseStockQuantity(1L, new DecreaseStockQuantityRequest(1)))
                .thenThrow(new InsufficientStockException("재고가 부족합니다."));
        mockMvc.perform(post("/api/products/1/stock/decrease")
                        .contentType(APPLICATION_JSON).content("{\"quantity\":1}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("INSUFFICIENT_STOCK_QUANTITY"));
    }

    private void assertInvalidRequest(String direction, String body) throws Exception {
        mockMvc.perform(post("/api/products/1/stock/" + direction)
                        .contentType(APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("INVALID_INPUT"))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field").value("quantity"));
        verifyNoInteractions(productRegister, productFinder);
    }

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
    @DisplayName("입고 수량이 1,000,000을 초과하면 quantity 필드 오류와 400을 반환한다")
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
}
