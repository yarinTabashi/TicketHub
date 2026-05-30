package com.yarin.common_dtos.order;

import com.yarin.common_dtos.order.api.OrderRequest;

public interface OrderService {
    int createOrder(OrderRequest orderRequest);
}
