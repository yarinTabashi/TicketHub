package com.yarin.order.order;

import java.util.List;

public record OrderRequest(
        String customerId,
        List<SeatRequest> seats
) {
}
