package com.study.commerce_lab.application.service;

import com.study.commerce_lab.adapter.in.webapi.reponse.IncreaseStockQuantityResponse;
import com.study.commerce_lab.adapter.in.webapi.request.IncreaseStockQuantityRequest;
import com.study.commerce_lab.application.port.in.ProductFinder;
import com.study.commerce_lab.application.port.in.ProductRegister;
import com.study.commerce_lab.application.port.out.ProductRepository;
import com.study.commerce_lab.domain.product.Product;
import com.study.commerce_lab.domain.product.exception.DuplicateSkuException;
import com.study.commerce_lab.adapter.in.webapi.request.ProductRegisterRequest;
import com.study.commerce_lab.domain.product.exception.ProductNotFoundException;
import com.study.commerce_lab.domain.product.exception.StockQuantityExceededException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ProductModifyService implements ProductRegister {
    private final ProductRepository productRepository;

    @Override
    public Product registerProduct(ProductRegisterRequest request) {
        checkDuplicateSku(request);

        Product product = Product.create(request);

        return productRepository.save(product);
    }

    @Override
    public IncreaseStockQuantityResponse increaseStockQuantity(Long productId, IncreaseStockQuantityRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        product.increaseStockQuantity(request.quantity());

        if(product.getStockQuantity() > 1000000)
            throw new StockQuantityExceededException("재고 수량 초과입니다.");

        return new IncreaseStockQuantityResponse(product.getId(), product.getStockQuantity());
    }

    private void checkDuplicateSku(ProductRegisterRequest request) {
        if(productRepository.findBySku(request.sku()).isPresent()){
            throw new DuplicateSkuException("이미 사용중인 SKU: " + request.sku());
        }
    }
}
