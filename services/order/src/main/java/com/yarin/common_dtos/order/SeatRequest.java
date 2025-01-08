package com.yarin.common_dtos.order;

public record SeatRequest(
        Integer screeningId,
        String seatNumber
) {
}
