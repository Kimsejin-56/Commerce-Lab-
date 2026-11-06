package com.study.commerce_lab.adapter.in.webapi;

import com.study.commerce_lab.adapter.in.webapi.reponse.DecreaseStockQuantityResponse;
import com.study.commerce_lab.adapter.in.webapi.reponse.IncreaseStockQuantityResponse;
import com.study.commerce_lab.adapter.in.webapi.reponse.ProductRegisterResponse;
import com.study.commerce_lab.domain.product.request.DecreaseStockQuantityRequest;
import com.study.commerce_lab.domain.product.request.IncreaseStockQuantityRequest;
import com.study.commerce_lab.domain.product.request.ProductRegisterRequest;
import com.study.commerce_lab.application.port.in.ProductFinder;
import com.study.commerce_lab.application.port.in.ProductRegister;
import com.study.commerce_lab.application.port.in.ProductStockManager;
import com.study.commerce_lab.domain.product.Product;
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
    private final ProductStockManager productStockManager;

    @PostMapping
    public ResponseEntity<ProductRegisterResponse> registerProduct(@Valid @RequestBody ProductRegisterRequest request) {
        Product product = productRegister.registerProduct(request);

        return ResponseEntity.status(201)
                .body(ProductRegisterResponse.from(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductRegisterResponse> findProduct(@PathVariable Long id) {
        Product product = productFinder.findProductById(id);

        return ResponseEntity.ok()
                .body(ProductRegisterResponse.from(product));
    }

    @PostMapping("/{id}/stock/increase")
    public ResponseEntity<IncreaseStockQuantityResponse> increaseStockQuantity(@PathVariable Long id, @Valid @RequestBody IncreaseStockQuantityRequest request) {
        Product product = productStockManager.increaseStockQuantity(id, request);

        return ResponseEntity.ok()
                .body(new IncreaseStockQuantityResponse(product.getId(), product.getStockQuantity()));
    }

    @PostMapping("/{id}/stock/decrease")
    public ResponseEntity<DecreaseStockQuantityResponse> decreaseStockQuantity(@PathVariable Long id, @Valid @RequestBody DecreaseStockQuantityRequest request) {
        Product product = productStockManager.decreaseStockQuantity(id, request);

        return ResponseEntity.ok()
                .body(new DecreaseStockQuantityResponse(product.getId(), product.getStockQuantity()));
    }
}
