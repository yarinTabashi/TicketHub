package com.yarin.common_dtos.screening;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(
        name = "screening-url",
        url = "${application.config.screening-url}"
)
public interface ScreeningClient {
    // This method checks if the screening and the seat exist and are available.
    // If so, it marks the seat as unavailable and returns the price for the ticket.
    @GetMapping("/exists/{screening-id}/{seat-number}")
    ResponseEntity<BigDecimal> validateAndReserveSeat(@PathVariable("screening-id") Integer screeningId,
                                            @PathVariable("seat-number") String seatNumber);

    // This method cancels the seat reservation
    @DeleteMapping("/cancel/{screening-id}/{seat-number}")
    void cancelSeatReservation(@PathVariable("screening-id") Integer screeningId,
                               @PathVariable("seat-number") String seatNumber);
}