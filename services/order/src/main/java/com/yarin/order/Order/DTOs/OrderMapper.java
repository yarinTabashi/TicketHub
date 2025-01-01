package com.yarin.order.Order.DTOs;

import com.yarin.order.Order.Order;
import java.util.UUID;

public class OrderMapper {
    public Order toOrder(OrderRequest request){
        if (request == null){
            return null;
        }

        return Order.builder()
                .orderNumber(UUID.randomUUID().toString()) // Create unique uuid number
                .customerId(request.customerId())
                .orderItem(request.orderItem())
                .totalAmount(request.totalAmount())
                .build();
    }

    public OrderResponse toResponse(Order order) {
        if (order == null) {
            return null;
        }
        return new OrderResponse(
                order.getOrderNumber(),
                order.getTotalAmount(),
                order.getCustomerId(),
                order.getOrderItem(),
                order.getCreatedDate(),
                order.getLastModifiedDate()
        );
    }
}
