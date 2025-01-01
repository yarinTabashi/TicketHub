package com.yarin.tickets.DTOs;

public record TicketRequest(
        Integer screeningId,
        Integer customerId,
        String seatNumber
) {}