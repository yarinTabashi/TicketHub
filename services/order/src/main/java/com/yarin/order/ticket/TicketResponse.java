package com.yarin.order.ticket;

import java.math.BigDecimal;

public record TicketResponse(
        Integer screeningId,
        String seatNumber,
        BigDecimal price
) {
}