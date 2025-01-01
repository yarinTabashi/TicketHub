package com.yarin.tickets.Tickets;

import com.yarin.tickets.DTOs.TicketRequest;
import com.yarin.tickets.DTOs.TicketResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // Create a new ticket
    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@RequestBody TicketRequest ticketRequest) {
        TicketResponse createdTicket = ticketService.createTicket(ticketRequest);
        return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
    }

    // Get all tickets for a specific screening
    @GetMapping("/screening/{screeningId}")
    public ResponseEntity<List<TicketResponse>> getTicketsByScreening(@PathVariable Integer screeningId) {
        List<TicketResponse> tickets = ticketService.getTicketsByScreening(screeningId);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    // Get all tickets for a specific customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<TicketResponse>> getTicketsByCustomer(@PathVariable Integer customerId) {
        List<TicketResponse> tickets = ticketService.getTicketsByCustomer(customerId);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    // Get a specific ticket by ID
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable Integer id) {
        Optional<TicketResponse> ticket = ticketService.getTicketById(id);
        return ticket.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a ticket by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Integer id) {
        ticketService.deleteTicket(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
