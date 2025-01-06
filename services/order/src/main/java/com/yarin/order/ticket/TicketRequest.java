package com.yarin.order.ticket;

import java.math.BigDecimal;

public record TicketRequest(
        Integer screeningId,
        String seatNumber,
        String customerId,
        BigDecimal ticketPrice
) {
}
