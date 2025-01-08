package com.yarin.common_dtos;
import java.math.BigDecimal;

public record Ticket(
        Integer ticketId,
        Integer screeningId,
        String seatNumber,
        BigDecimal amount
) {
}
