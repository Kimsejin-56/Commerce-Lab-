package com.study.commerce_lab.adapter.in.webapi;

import com.study.commerce_lab.adapter.in.webapi.reponse.OrderResponse;
import com.study.commerce_lab.application.port.in.OrderCreater;
import com.study.commerce_lab.application.port.in.OrderFinder;
import com.study.commerce_lab.domain.order.request.OrderRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderCreater orderCreater;
    private final OrderFinder orderFinder;

    @PostMapping()
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponse.from(orderCreater.createOrder(request)));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> findOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok().body(OrderResponse.from(orderFinder.findOrderById(orderId)));
    }
}
