package com.study.commerce_lab.application;

import com.study.commerce_lab.adapter.webapi.ProductRegisterResponse;
import com.study.commerce_lab.application.provided.ProductRegister;
import com.study.commerce_lab.application.required.ProductRepository;
import com.study.commerce_lab.domain.product.Product;
import com.study.commerce_lab.domain.product.ProductRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ProductModifyService implements ProductRegister {
    private final ProductRepository productRepository;

    @Override
    public ProductRegisterResponse registerProduct(ProductRegisterRequest request) {
        Product product = Product.create(request);

        Product save = productRepository.save(product);

        return new ProductRegisterResponse(save.getId(), save.getSku(),
                save.getName(), save.getPrice(), save.getStockQuantity());
    }
}
