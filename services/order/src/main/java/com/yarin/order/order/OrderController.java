package com.yarin.order.order;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Integer> createOrder(@RequestBody OrderRequest request) {
        return ResponseEntity.ok(this.orderService.createOrder(request));
    }

    // TODO: Add endpoint: GET get all orders by {customer-id}
    // TODO: Add endpoint: DELETE cancel order by {order-id}
}
