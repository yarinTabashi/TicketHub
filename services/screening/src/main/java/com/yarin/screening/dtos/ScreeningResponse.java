package com.yarin.screening.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ScreeningResponse(
        Integer id,
        Integer movieId,
        LocalDateTime showtime,
        Integer availableSeats,
        BigDecimal ticketPrice
) {
}