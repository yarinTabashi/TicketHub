package com.yarin.order.Order;

import com.yarin.order.Order.DTOs.OrderMapper;
import com.yarin.order.Order.DTOs.OrderRequest;
import com.yarin.order.Order.DTOs.OrderResponse;
import com.yarin.order.OrderItem.OrderItem;
import com.yarin.order.customer.CustomerClient;
import com.yarin.order.exceptions.IncompatibilityException;
import com.yarin.order.screening.ScreeningClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CustomerClient customerClient;
    private final ScreeningClient screeningClient;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    @Transactional
    public boolean createOrder(OrderRequest orderRequest){
        // 1. Validate the customer exists
        boolean isUserExist = customerClient.isExist(orderRequest.customerId());
        if (!isUserExist){
            throw new IncompatibilityException("Customer not found");
        }

        // 2. Validate the screening exists, and make sure there are tickets left in stock.
        OrderItem orderItem = orderRequest.orderItem();
        int remainingTickets = 0;
        try {
            remainingTickets = screeningClient.getRemainingTickets(orderItem.getScreeningId());
        } catch (FeignException e) {
            throw new RuntimeException("Screening not found with ID: " + orderItem.getScreeningId());
        }

        if (remainingTickets <= 0) {
            throw new IncompatibilityException("No remaining tickets for this screening");
        }

        // 3. Compare calculated total with the totalAmount from the order request
        BigDecimal price = BigDecimal.valueOf(orderItem.getPrice());
        BigDecimal calculatedTotal = price.multiply(BigDecimal.valueOf(orderItem.getQuantity()));

        if (calculatedTotal.compareTo(orderRequest.totalAmount()) != 0) {
            throw new RuntimeException("There is an error in calculating the cost of the order");
        }

        // 3. Create and save the order

        // 4. Send confirmation notification (using kafka)

        return true;
    }

    public List<OrderResponse> findAllOrders() {
        // Fetch all orders from the repository
        List<Order> orders = orderRepository.findAll();

        // Map the list of Order entities to a list of OrderResponse DTOs
        return orders.stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }
}
