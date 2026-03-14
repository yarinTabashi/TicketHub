package com.yarin.common_dtos.order;

import java.math.BigDecimal;

public record OrderCreatedEvent(String customerId, String email, String fullName, Integer orderId, String orderCode, BigDecimal amount) {
}