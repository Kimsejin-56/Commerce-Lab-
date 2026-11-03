package com.study.commerce_lab.adapter.in.webapi;

import com.study.commerce_lab.adapter.in.webapi.reponse.ProductRegisterResponse;
import com.study.commerce_lab.adapter.in.webapi.reponse.AddStockQuantityResponse;
import com.study.commerce_lab.adapter.in.webapi.request.AddStockQuantityRequest;
import com.study.commerce_lab.application.port.in.ProductFinder;
import com.study.commerce_lab.application.port.in.ProductRegister;
import com.study.commerce_lab.domain.product.Product;
import com.study.commerce_lab.adapter.in.webapi.request.ProductRegisterRequest;
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
        Product product = productRegister.registerProduct(request);

        return ResponseEntity.status(201)
                .body(new ProductRegisterResponse(
                        product.getId(),
                        product.getSku(),
                        product.getName(),
                        product.getPrice(),
                        product.getStockQuantity()
                ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findProduct(@PathVariable Long id) {
        return ResponseEntity.ok()
                .body(productFinder.findProductById(id));
    }

    @PostMapping("/{id}/stock/increase")
    public ResponseEntity<AddStockQuantityResponse> addStockQuantity(@PathVariable Long id, @Valid @RequestBody AddStockQuantityRequest request) {
        return ResponseEntity.ok()
                .body(productRegister.addStockQuantity(id, request));
    }
}
