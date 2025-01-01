package com.yarin.screening.Screening;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "screening_seq")
//    @SequenceGenerator(name = "screening_seq", sequenceName = "screening_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private Integer movieId;
    private LocalDateTime showtime;  // Including both date and time
    private Integer availableSeats;
}
