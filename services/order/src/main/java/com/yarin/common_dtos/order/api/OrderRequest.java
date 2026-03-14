package com.yarin.common_dtos.order.api;

import com.yarin.common_dtos.order.SeatRequest;

import java.util.List;

public record OrderRequest(
        String customerId,
        List<SeatRequest> seats
) {
}
