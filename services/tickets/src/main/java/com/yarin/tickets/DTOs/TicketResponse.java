package com.yarin.tickets.DTOs;

import java.time.LocalDateTime;

public record TicketResponse(
        Integer id,
        Integer screeningId,
        Integer customerId,
        String seatNumber,
        TicketStatus ticketStatus,
        LocalDateTime purchaseTime
) {
}