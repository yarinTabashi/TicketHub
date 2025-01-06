package com.yarin.order.ticket;

import com.yarin.order.common_dtos.Ticket;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TicketMapper {
    // Convert TicketRequest to Ticket entity
    public Ticket toTicket(TicketRequest request) {
        if (request == null) {
            return null;
        }
        return Ticket.builder()
                .screeningId(request.screeningId())
                .seatNumber(request.seatNumber())
                .ticketPrice(new BigDecimal(0))
                .build();
    }

    // Convert Ticket entity to TicketResponse (or DTO)
    public TicketResponse fromTicket(Ticket ticket) {
        if (ticket == null) {
            return null;
        }
        return new TicketResponse(
                ticket.getScreeningId(),
                ticket.getSeatNumber(),
                ticket.getTicketPrice()
        );
    }
}
