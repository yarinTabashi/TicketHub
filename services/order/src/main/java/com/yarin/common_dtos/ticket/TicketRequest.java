package com.yarin.common_dtos.ticket;

import java.math.BigDecimal;

public record TicketRequest(
        Integer screeningId,
        String seatNumber,
        String customerId,
        BigDecimal ticketPrice
) {
}
