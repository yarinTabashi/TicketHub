package com.yarin.common_dtos.ticket;
import com.yarin.common_dtos.common_dtos.Ticket;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    public Ticket createTicket(TicketRequest request){
        if (request == null) {
            return null;
        }

        Ticket ticket = Ticket.builder()
                .screeningId(request.screeningId())
                .seatNumber(request.seatNumber())
                .ticketPrice(request.ticketPrice())
                .build();

        // Save the ticket in the database and return the ticket's ID
        return ticketRepository.save(ticket);
    }

    public boolean deleteTicket(Integer ticketId) {
        // Check if the ticket exists
        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);

        if (ticketOptional.isPresent()) {
            // Delete the ticket if it exists
            ticketRepository.deleteById(ticketId);
            return true;
        }

        // Return false if the ticket doesn't exist
        return false;
    }

    // Get all tickets for a specific screening
    @Transactional
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
}
