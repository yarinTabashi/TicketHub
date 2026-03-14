package com.yarin.common_dtos.screening;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(
        name = "screening-url",
        url = "${application.config.screening-url}"
)
public interface ScreeningClient {
    @GetMapping("/exists/{screening-id}/{seat-number}")
    ResponseEntity<Boolean> validateSeats(@PathVariable("screening-id") Integer screeningId,
                                             @PathVariable("seat-number") List<String> seatsNumbers);


    // This method checks if the screening and the seat exist and are available.
    // If so, it marks the seat as unavailable and returns the price for the ticket.
    @GetMapping("/exists/{screening-id}/{seat-number}")
    ResponseEntity<BigDecimal> validateAndReserveSeat(@PathVariable("screening-id") Integer screeningId,
                                            @PathVariable("seat-number") String seatNumber);

    // This method cancels the seat reservation
    @DeleteMapping("/cancel/{screening-id}/{seat-number}")
    void cancelSeatReservation(@PathVariable("screening-id") Integer screeningId,
                               @PathVariable("seat-number") String seatNumber);

    ResponseEntity<BigDecimal> getOrderAmount(Integer screeningId, List<String> requiredSeats);

    @PostMapping("/reserve/{screening-id}/{order-id}")
    ResponseEntity<Boolean> reserveSeats(
            @PathVariable("screening-id") Integer screeningId,
            @RequestBody List<String> seatNumbers
    );

    @PostMapping("/release/{screening-id}/{order-id}")
    ResponseEntity<Void> releaseSeats(
            @PathVariable("screening-id") Integer screeningId,
            @RequestBody List<String> seatNumbers
    );
}