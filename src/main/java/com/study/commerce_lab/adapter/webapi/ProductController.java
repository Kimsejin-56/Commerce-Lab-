package com.study.commerce_lab.adapter.webapi;

import com.study.commerce_lab.application.ProductModifyService;
import com.study.commerce_lab.application.provided.ProductRegister;
import com.study.commerce_lab.domain.product.ProductRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRegister productRegister;

    @PostMapping
    public ResponseEntity<ProductRegisterResponse> registerProduct(@RequestBody ProductRegisterRequest request) {
        return ResponseEntity.status(201).body(productRegister.registerProduct(request));
    }
}
