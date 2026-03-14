package com.yarin.common_dtos.order;

public record SeatRequest(
        Integer screeningId, // TODO: Should be in SeatRequest? Maybe each purchase is for a single screening
        String seatNumber
) {
}
