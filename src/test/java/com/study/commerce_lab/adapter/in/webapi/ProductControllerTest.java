package com.study.commerce_lab.adapter.in.webapi;

import com.study.commerce_lab.application.port.in.ProductFinder;
import com.study.commerce_lab.application.port.in.ProductRegister;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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
    ProductFinder productFinder;

    @Test
    void 입고수량이_0이면_입력값_검증에러를_반환한다() throws Exception {
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
    void 입고수량이_최대범위를_초과하면_입력값_검증에러를_반환한다() throws Exception {
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
