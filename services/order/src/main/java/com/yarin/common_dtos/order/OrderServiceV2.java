package com.yarin.common_dtos.order;

import ch.qos.logback.classic.Logger;
import com.yarin.common_dtos.customer.CustomerClient;
import com.yarin.common_dtos.order.api.OrderRequest;
import com.yarin.common_dtos.payment.PaymentClient;
import com.yarin.common_dtos.screening.ScreeningClient;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Service
@ConditionalOnProperty(name = "order.service.with-publish", havingValue = "true")
public class OrderServiceV2 implements OrderService {
    private final OrderRepository repository;
    private final CustomerClient customerClient;
    private final ScreeningClient screeningClient;
    private final PaymentClient paymentClient;
    private Logger log;
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    @Override
    public int createOrder(OrderRequest orderRequest) {
        return 0; //TODO
    }

    public int createOrder(String customerId, Integer screeningId, List<String> requiredSeats) {
        // Steps 1 & 2: Make 2 async calls in parallel (validate user exist and seats availability)
        CompletableFuture<Boolean> isCustomerExist = verifyCustomer(customerId);
        CompletableFuture<Boolean> areSeatsAvailable = verifySeats(screeningId, requiredSeats);

        // Wait for both async results
        boolean customerExists = isCustomerExist.join();
        boolean seatsAvailable = areSeatsAvailable.join();

        // If either validation fails, return failure response
        if (!customerExists || !seatsAvailable) {
            return -1;
        }

        CompletableFuture<BigDecimal> orderAmount = getOrderAmount(screeningId, requiredSeats);

        // Step 3: Create and save the order with PENDING status
        Order order = createInitialOrder(customerId, orderAmount.join());

        // Step 4: Revalidate seat availability and mark them as "reserved" (Optimistic Locking)
        Boolean finalSeatsAvailability = screeningClient.reserveSeats(screeningId, requiredSeats).getBody();
        if (Boolean.FALSE.equals(finalSeatsAvailability)){
            // Rollback: Set OrderStatus-FAILED
            return handleOrderFailure(order, "Seat reservation failed");
        }

        // Step 5: Make a calling to the payment service
        Boolean isPaymentSucceed = paymentClient.executePayment(customerId, orderAmount.join()).getBody();
        if (Boolean.FALSE.equals(isPaymentSucceed)){
            // Rollback: 1) Set OrderStatus-FAILED | 2) Release the seats (Update the inventory On Screening, and delete the tickets on Tickets)
            return handlePaymentFailure(order, screeningId, requiredSeats);
        }

        // TODO: Step 6: Publish OrderCreated event (will be consumed by Notification-service
        //  and by the Order-service: it should update the order status to succeed and set a serial num)
        createOrderCreatedEvent(customerId, order.getId(), order.getTotalAmount(), order.getOrderCode());
        return order.getId();
    }

    @Async
    private void createOrderCreatedEvent(String customerId, Integer orderId, BigDecimal orderAmount, String orderCode) {
        try{
            CompletableFuture<String> customerFullName = customerClient.getCustomerFullName(customerId);
            CompletableFuture<String> customerEmail = customerClient.getCustomerEmail(customerId);

            String customerFullNameSt = customerFullName.get();
            String customerEmailSt = customerEmail.get();

            // Step 3: Create the event with retrieved details
            OrderCreatedEvent event = new OrderCreatedEvent(
                    customerId,
                    customerEmailSt,
                    customerFullNameSt,
                    orderId,
                    orderCode,
                    orderAmount
            );
            kafkaTemplate.send(new ProducerRecord<>("orders-events", event));
        }
        catch (Exception e){
            log.error("Failed to publish OrderCreatedEvent for Order ID: {}", orderId, e);
        }
    }

    @Async
    private CompletableFuture<Boolean> verifyCustomer(String customerId) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        try {
            boolean isExist = customerClient.verifyCustomer(customerId);
            future.complete(isExist);
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    @Async
    private CompletableFuture<Boolean> verifySeats(Integer screeningId, List<String> requiredSeats) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        try {
            ResponseEntity<Boolean> response = screeningClient.validateSeats(screeningId, requiredSeats);
            future.complete(response.getBody());
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    @Async
    private CompletableFuture<BigDecimal> getOrderAmount(Integer screeningId, List<String> requiredSeats){
        CompletableFuture<BigDecimal> future = new CompletableFuture<>();
        try {
            ResponseEntity<BigDecimal> response = screeningClient.getOrderAmount(screeningId, requiredSeats);
            future.complete(response.getBody());
        }
        catch (Exception e){
            future.completeExceptionally(e);
        }
        return future;
    }

    private Order createInitialOrder(String customerId, BigDecimal orderAmount){
        Order order = new Order();
        order.setCustomerId(customerId);
        order.setTickets(null);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(orderAmount);
        repository.save(order);

        return order;
    }

    private int handleOrderFailure(Order order, String reason) {
        order.setStatus(OrderStatus.FAILED);
        repository.save(order); // Persist status update
        log.error("Order {} failed: {}", order.getId(), reason);
        return order.getId();
    }

    private int handlePaymentFailure(Order order, Integer screeningId, List<String> requiredSeats) {
        screeningClient.releaseSeats(screeningId, requiredSeats);
        handleOrderFailure(order, "Payment failed. Seats released. Order ID: " + order.getId());
        return order.getId();
    }


}
