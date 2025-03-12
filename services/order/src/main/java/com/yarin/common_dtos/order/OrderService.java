package com.yarin.common_dtos.order;

import com.yarin.common_dtos.common_dtos.Ticket;
import com.yarin.common_dtos.customer.CustomerClient;
import com.yarin.common_dtos.exceptions.IncompatibilityException;
import com.yarin.common_dtos.exceptions.InventoryException;
import com.yarin.common_dtos.exceptions.PaymentException;
import com.yarin.common_dtos.notification.NotificationClient;
import com.yarin.common_dtos.payment.PaymentClient;
import com.yarin.common_dtos.screening.ScreeningClient;
import com.yarin.common_dtos.ticket.TicketRequest;
import com.yarin.common_dtos.ticket.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@AllArgsConstructor
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class); // Initialize the logger

    private final OrderRepository orderRepository;
    private final TicketService ticketService;

    private final CustomerClient customerClient;
    private final ScreeningClient screeningClient;
    private final PaymentClient paymentClient;
    private final NotificationClient notificationClient;
    private List<Ticket> tickets;
    private ApplicationEventPublisher eventPublisher;


    public int createOrder(OrderRequest orderRequest) {
        try {
            // 1. Verify customer exists
            verifyCustomerExists(orderRequest.customerId());

            // 2. Verify required seats are available and calculate total price
            validateSeatsAndGenerateTickets(orderRequest, orderRequest.seats());

            // 3. Create order with 'PENDING' status
            Order order = createPendingOrder(orderRequest, tickets);

            // 4. Make call to the payment service
            processPayment(orderRequest.customerId(), order);

            // 5. TODO: Create event of OrderSucceeded for notification service to consume it

            // 6. Update the order status to SUCCESS
            return completeOrder(order);
        } catch (IncompatibilityException | InventoryException | PaymentException e) {
            // Handle compensation (rollback)
            handleFailure(tickets);

            throw e;
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            handleFailure(tickets);

            throw new RuntimeException("An unexpected error occurred while processing the order.", e);
        }
    }

    // Verify if the customer exists
    private void verifyCustomerExists(String customerId) {
        boolean isUserExists = false;
        try {
            isUserExists = customerClient.verifyCustomer(customerId);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while trying to contact the customer-service", e);
        }

        if (!isUserExists) {
            throw new IncompatibilityException("Customer not found");
        }
    }

    // Verify seats and creates tickets asynchronously
    private void validateSeatsAndGenerateTickets(OrderRequest orderRequest, List<SeatRequest> seats) {
        // List to hold futures of the asynchronous seat validations
        List<CompletableFuture<Void>> seatValidationFutures = seats.stream()
                .map(seatRequest -> CompletableFuture.runAsync(() -> {
                    try {
                        // Validate and reserve each seat asynchronously
                        ResponseEntity<BigDecimal> seatValidationResponse = validateAndReserveSeat(seatRequest);

                        // If seat is valid, create a ticket
                        BigDecimal price = seatValidationResponse.getBody();
                        if (price != null) {
                            // Add ticket creation logic
                            tickets.add(ticketService.createTicket(new TicketRequest(seatRequest.screeningId(), seatRequest.seatNumber(), orderRequest.customerId(), price)));
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("Error processing seat request for seat: " + seatRequest.seatNumber(), e);
                    }
                }))
                .collect(Collectors.toList());

        // Wait for all asynchronous seat validation tasks to complete
        CompletableFuture.allOf(seatValidationFutures.toArray(new CompletableFuture[0])).join();
    }

    // Validate and reserve a seat
    private ResponseEntity<BigDecimal> validateAndReserveSeat(SeatRequest seatRequest) {
        ResponseEntity<BigDecimal> seatValidationResponse;
        try {
            seatValidationResponse = screeningClient.validateAndReserveSeat(seatRequest.screeningId(), seatRequest.seatNumber());
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while trying to contact the screening-service", e);
        }

        if (seatValidationResponse.getStatusCode() != HttpStatus.OK || seatValidationResponse.getBody() == null) {
            throw new IncompatibilityException("One of your required seats is either non-existent or not available");
        }

        return seatValidationResponse;
    }

    // Calculate the total amount for all the tickets in the list
    private BigDecimal getTotalAmount(List<Ticket> tickets) {
        // Initialize the total amount as zero
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Ticket ticket : tickets) {
            totalAmount = totalAmount.add(ticket.getTicketPrice());
        }

        return totalAmount;
    }

    // Create a pending order
    private Order createPendingOrder(OrderRequest orderRequest, List<Ticket> tickets) {
        BigDecimal totalAmount = getTotalAmount(tickets);

        Order order = Order.builder()
                .customerId(orderRequest.customerId())
                .totalAmount(totalAmount)
                .status(OrderStatus.PENDING)
                .tickets(tickets)
                .build();

        return orderRepository.save(order);
    }

    // Process payment
    private void processPayment(String customerId, Order order) {
        boolean isPaymentSucceed;
        try {
            isPaymentSucceed = paymentClient.makePayment(customerId);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while trying to contact the payment-service", e);
        }

        if (!isPaymentSucceed) {
            order.setStatus(OrderStatus.FAILED);
            orderRepository.save(order);
            throw new IncompatibilityException("Payment failed");
        }
    }

    // Helper Function 6: Send notification
    private void sendNotification(String customerId, Order order) {
        boolean isNotificationSucceed;
        try {
            isNotificationSucceed = notificationClient.sendOrderSucceedNotification(customerId);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while trying to contact the notification-service", e);
        }

        if (!isNotificationSucceed) {
            order.setStatus(OrderStatus.NOTIFICATION_FAILED);
            orderRepository.save(order);
            throw new RuntimeException("Failed to send notification to the customer");
        }
    }

    // Complete order and return its ID
    private int completeOrder(Order order) {
        order.setStatus(OrderStatus.SUCCESS);
        orderRepository.save(order);
        return order.getId();
    }

    private void handleFailure(List<Ticket> tickets) {
        for (Ticket ticket : tickets) {
            try {
                // Delete the ticket using its ID
                ticketService.deleteTicket(ticket.getId());
                // Release the reserved seat
                releaseSeat(ticket);
            } catch (Exception e) {
                // Log error if ticket deletion or seat release fails
                log.error("Failed to process failure for ticket with ID: {}", ticket.getId(), e);
            }
        }
    }

    private void releaseSeat(Ticket ticket) {
        try {
            screeningClient.cancelSeatReservation(ticket.getScreeningId(), ticket.getSeatNumber());
        } catch (Exception e) {
            log.error("Failed to release seat {} for screening {}", ticket.getSeatNumber(), ticket.getScreeningId(), e);
        }
    }
}