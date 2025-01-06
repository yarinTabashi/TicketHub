package com.yarin.order.order;

public record SeatRequest(
        Integer screeningId,
        String seatNumber
) {
}
