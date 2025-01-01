package com.yarin.tickets.Tickets;

import com.yarin.tickets.DTOs.TicketStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_seq")
    @SequenceGenerator(name = "ticket_seq", sequenceName = "ticket_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "screening_id")
    private Integer screeningId;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "seat_number")
    private String seatNumber;

    @Enumerated(EnumType.STRING) // Ensures the enum is stored as a string in the database
    @Column(name = "ticket_status")
    private TicketStatus ticketStatus;

    @Column(name = "purchase_time")
    private LocalDateTime purchaseTime;
}