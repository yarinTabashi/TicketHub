package com.yarin.common_dtos.customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.CompletableFuture;

@FeignClient(
        name = "customer-service",
        url = "${application.config.customer-url}"
)
public interface CustomerClient {

    @GetMapping("/exists/{customer-id}")
    boolean verifyCustomer(@PathVariable("customer-id") String customerId);

    @GetMapping("/customer/{customer-id}/fullname")
    CompletableFuture<String> getCustomerFullName(@PathVariable("customer-id") String customerId);

    @GetMapping("/customer/{customer-id}/email")
    CompletableFuture<String> getCustomerEmail(@PathVariable("customer-id") String customerId);
}