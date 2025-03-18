package com.yarin.screening.screening;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name="screening")
public class Screening {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @Version
    private Long version;  // used for handle optimistic locking
    private Integer movieId;
    private LocalDateTime showtime;
    @Lob
    private String seatsAvailabilityMap; // 0-Available, 1-Booked, 2- Unavailable seat
    private Integer availableSeats; // cached for improve performance to avoid recalculating it from the map.
    private BigDecimal ticketPrice;
}