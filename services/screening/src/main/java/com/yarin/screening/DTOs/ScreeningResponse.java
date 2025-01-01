package com.yarin.screening.DTOs;

import java.time.LocalDateTime;

public record ScreeningResponse(
        Integer id,
        Integer movieId,
        LocalDateTime showtime,
        Integer availableSeats
) {
}