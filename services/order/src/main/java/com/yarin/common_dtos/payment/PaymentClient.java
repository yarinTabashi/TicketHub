package com.yarin.common_dtos.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "payment-url",
        url = "${application.config.payment-url}"
)
public interface PaymentClient {
    @GetMapping("/pay/{customer-id}")
    boolean makePayment(@PathVariable("customer-id") String customerId);
}