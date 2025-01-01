package com.yarin.tickets.Tickets;

import com.yarin.tickets.DTOs.TicketMapper;
import com.yarin.tickets.DTOs.TicketRequest;
import com.yarin.tickets.DTOs.TicketResponse;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    public TicketService(TicketRepository ticketRepository, TicketMapper ticketMapper) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
    }

    // Create a new ticket
    public TicketResponse createTicket(TicketRequest ticketRequest) {
        // Convert the TicketRequest to Ticket entity
        Ticket ticket = ticketMapper.toTicket(ticketRequest);
        // Save the ticket to the database
        Ticket savedTicket = ticketRepository.save(ticket);
        // Convert the saved Ticket entity to TicketResponse
        return ticketMapper.fromTicket(savedTicket);
    }

    // Get all tickets for a specific screening
    public List<TicketResponse> getTicketsByScreening(Integer screeningId) {
        List<Ticket> tickets = ticketRepository.findByScreeningId(screeningId);
        return tickets.stream()
                .map(ticketMapper::fromTicket)
                .collect(Collectors.toList());
    }

    // Get all tickets for a specific customer
    public List<TicketResponse> getTicketsByCustomer(Integer customerId) {
        List<Ticket> tickets = ticketRepository.findByCustomerId(customerId);
        return tickets.stream()
                .map(ticketMapper::fromTicket)
                .collect(Collectors.toList());
    }

    // Get a specific ticket by ID
    public Optional<TicketResponse> getTicketById(Integer id) {
        return ticketRepository.findById(id).map(ticketMapper::fromTicket);
    }

    // Delete a ticket by ID
    public void deleteTicket(Integer id) {
        ticketRepository.deleteById(id);
    }
}
