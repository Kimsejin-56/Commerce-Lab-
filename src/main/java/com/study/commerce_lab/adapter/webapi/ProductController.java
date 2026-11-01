package com.study.commerce_lab.adapter.webapi;

import com.study.commerce_lab.adapter.webapi.reponse.ProductRegisterResponse;
import com.study.commerce_lab.application.provided.ProductFinder;
import com.study.commerce_lab.application.provided.ProductRegister;
import com.study.commerce_lab.domain.product.Product;
import com.study.commerce_lab.domain.product.request.ProductRegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRegister productRegister;
    private final ProductFinder productFinder;

    @PostMapping
    public ResponseEntity<ProductRegisterResponse> registerProduct(@Valid @RequestBody ProductRegisterRequest request) {
        return ResponseEntity.status(201)
                .body(productRegister.registerProduct(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findProduct(@PathVariable Long id) {
        return ResponseEntity.ok()
                .body(productFinder.findProductById(id));
    }
}
