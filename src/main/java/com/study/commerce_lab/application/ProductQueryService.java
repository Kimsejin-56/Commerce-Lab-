package com.study.commerce_lab.application;

import com.study.commerce_lab.application.provided.ProductFinder;
import com.study.commerce_lab.application.required.ProductRepository;
import com.study.commerce_lab.domain.product.Product;
import com.study.commerce_lab.domain.product.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductQueryService implements ProductFinder {

    private final ProductRepository productRepository;


    @Override
    public Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
