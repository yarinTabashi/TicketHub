package com.yarin.screening.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ScreeningRequest(
        @NotNull(message = "Movie ID cannot be null.")
        Integer movieId,

        @NotNull(message = "Showtime cannot be null.")
        LocalDateTime showtime,

        @Min(value = 1, message = "Number of rows must be at least 1.")
        @Max(value=100, message = "Number of rows must be less than 100.")
        Integer numOfRows,

        @Min(value = 1, message = "Number of seats in each row must be at least 1.")
        @Max(value=100, message = "Number of columns must be less than 100.")
        Integer seatsPerRow,
        @Min(value = 1, message = "Ticket price must be more than 1.")
        @Max(value=300, message = "Ticket price must be less than 300.")
        BigDecimal ticketPrice
) {
}