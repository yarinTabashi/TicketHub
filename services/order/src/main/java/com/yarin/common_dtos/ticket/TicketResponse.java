package com.yarin.common_dtos.ticket;

import java.math.BigDecimal;

public record TicketResponse(
        Integer screeningId,
        String seatNumber,
        BigDecimal price
) {
}