package com.yarin.common_dtos.ticket;

import com.yarin.common_dtos.common_dtos.Ticket;
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
    public ResponseEntity<Ticket> createTicket(@RequestBody TicketRequest ticketRequest) {
        Ticket ticket = ticketService.createTicket(ticketRequest);
        return new ResponseEntity<>(ticket, HttpStatus.CREATED);
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Integer ticketId) {
        boolean isDeleted = ticketService.deleteTicket(ticketId);

        if (isDeleted) {
            return ResponseEntity.noContent().build();  // 204 No Content
        } else {
            return ResponseEntity.notFound().build();  // 404 Not Found if ticket doesn't exist
        }
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
}
