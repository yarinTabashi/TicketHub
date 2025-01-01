package com.yarin.order.Order.DTOs;

import com.yarin.order.OrderItem.OrderItem;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record OrderRequest(
        @NotNull(message = "Customer can't be null")
        String customerId,
        @NotNull(message = "Order item cannot be null")
        OrderItem orderItem,
        @Positive(message = "The amount mush be positive")
        BigDecimal totalAmount
) {
}