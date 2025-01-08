package com.yarin.common_dtos.order;

import java.util.List;

public record OrderRequest(
        String customerId,
        List<SeatRequest> seats
) {
}
