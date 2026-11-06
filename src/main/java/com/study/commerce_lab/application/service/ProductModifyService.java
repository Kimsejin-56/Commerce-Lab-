package com.study.commerce_lab.application.service;

import com.study.commerce_lab.domain.product.request.DecreaseStockQuantityRequest;
import com.study.commerce_lab.domain.product.request.IncreaseStockQuantityRequest;
import com.study.commerce_lab.domain.product.request.ProductRegisterRequest;
import com.study.commerce_lab.application.port.in.ProductRegister;
import com.study.commerce_lab.application.port.in.ProductStockManager;
import com.study.commerce_lab.application.port.out.ProductRepository;
import com.study.commerce_lab.domain.product.Product;
import com.study.commerce_lab.domain.product.exception.DuplicateSkuException;
import com.study.commerce_lab.domain.product.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ProductModifyService implements ProductRegister, ProductStockManager {
    private final ProductRepository productRepository;

    @Override
    public Product registerProduct(ProductRegisterRequest request) {
        checkDuplicateSku(request);

        Product product = Product.create(request);

        return productRepository.save(product);
    }

    @Override
    public Product increaseStockQuantity(Long productId, IncreaseStockQuantityRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        product.increaseStockQuantity(request.quantity());

        return product;
    }

    @Override
    public Product decreaseStockQuantity(Long productId, DecreaseStockQuantityRequest request) {
        Product product=productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        product.decreaseStockQuantity(request.quantity());

        return product;
    }

    private void checkDuplicateSku(ProductRegisterRequest request) {
        if(productRepository.findBySku(request.sku()).isPresent()){
            throw new DuplicateSkuException("이미 사용중인 SKU: " + request.sku());
        }
    }
}
