package com.yarin.common_dtos.order;

import java.math.BigDecimal;

public record OrderSucceededEvent(
        Integer orderId,
        String customerId,
        BigDecimal amount
) {
}
