package com.study.commerce_lab.adapter.in.webapi;

import com.study.commerce_lab.adapter.in.webapi.reponse.CreateOrderResponse;
import com.study.commerce_lab.application.port.in.OrderCreater;
import com.study.commerce_lab.application.port.out.OrderRepository;
import com.study.commerce_lab.application.service.OrderModifyService;
import com.study.commerce_lab.domain.order.Order;
import com.study.commerce_lab.domain.order.request.CreateOrderItemRequest;
import com.study.commerce_lab.domain.order.request.CreateOrderRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderCreater orderCreater;

    @PostMapping()
    public ResponseEntity<CreateOrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(CreateOrderResponse.from(orderCreater.createOrder(request)));
    }
}
