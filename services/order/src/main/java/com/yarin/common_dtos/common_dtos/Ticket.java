package com.yarin.common_dtos.common_dtos;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

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

    @Column(name = "seat_number")
    private String seatNumber;

    @Column(name = "ticket_price")
    private BigDecimal ticketPrice;
}