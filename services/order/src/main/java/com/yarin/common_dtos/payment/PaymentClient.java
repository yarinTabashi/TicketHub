package com.yarin.common_dtos.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(
        name = "payment-url",
        url = "${application.config.payment-url}"
)
public interface PaymentClient {
    @GetMapping("/pay/{customer-id}")
    ResponseEntity<Boolean> executePayment(
            @PathVariable("customer-id") String customerId,
            @RequestParam("amount") BigDecimal amount
    );
}