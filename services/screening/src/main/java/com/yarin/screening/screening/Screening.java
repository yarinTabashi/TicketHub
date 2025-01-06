package com.yarin.screening.screening;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private Integer movieId;
    private LocalDateTime showtime;
    private boolean[][] seatsAvailabilityMap;
    private Integer availableSeats; // cached for improve performance to avoid recalculating it from the map.
}