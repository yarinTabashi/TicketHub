package com.yarin.order.Order.DTOs;

import com.yarin.order.OrderItem.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse (
    String orderNumber,
    BigDecimal totalAmount,
    String customerId,
    OrderItem orderItem,
    LocalDateTime createdDate,
    LocalDateTime lastModifiedDate
){

}