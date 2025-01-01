package com.yarin.order.screening;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "screening-url",
        url = "${application.config.screening-url}"
)
public interface ScreeningClient {
    // Get the remaining tickets for a screening
    @GetMapping("/remaining-tickets/{screening-id}")
    int getRemainingTickets(@PathVariable("screening-id") Integer screeningId);

    @GetMapping("/exists/{screening-id}")
    boolean isExist(@PathVariable("screening-id") String customerId);
}