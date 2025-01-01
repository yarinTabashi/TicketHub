package com.yarin.screening.DTOs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ScreeningRequest(
        @NotNull(message = "Movie ID cannot be null.")
        Integer movieId,

        @NotNull(message = "Showtime cannot be null.")
        LocalDateTime showtime,

        @Min(value = 1, message = "Available seats must be at least 1.")
        Integer availableSeats
) {
}