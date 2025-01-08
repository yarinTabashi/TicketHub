package com.yarin.common_dtos;

import java.math.BigDecimal;
import java.util.List;

public record Order(
        List<Ticket> tickets,
        String orderNumber,
        BigDecimal totalAmount
) {
}