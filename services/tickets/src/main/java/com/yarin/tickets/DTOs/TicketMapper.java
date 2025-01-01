package com.yarin.tickets.DTOs;

import com.yarin.tickets.Tickets.Ticket;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class TicketMapper {
    // Convert TicketRequest to Ticket entity
    public Ticket toTicket(TicketRequest request) {
        if (request == null) {
            return null;
        }
        return Ticket.builder()
                .screeningId(request.screeningId())
                .customerId(request.customerId())
                .seatNumber(request.seatNumber())
                .ticketStatus(TicketStatus.RESERVED)  // Assuming ticketStatus is created on creation
                .purchaseTime(LocalDateTime.now())  // Assuming purchaseTime is set to now when ticket is created
                .build();
    }

    // Convert Ticket entity to TicketResponse (or DTO)
    public TicketResponse fromTicket(Ticket ticket) {
        if (ticket == null) {
            return null;
        }
        return new TicketResponse(
                ticket.getId(),
                ticket.getScreeningId(),
                ticket.getCustomerId(),
                ticket.getSeatNumber(),
                ticket.getTicketStatus(),
                ticket.getPurchaseTime()
        );
    }
}
