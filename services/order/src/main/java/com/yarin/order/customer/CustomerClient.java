package com.yarin.order.customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "customer-url",
        url = "${application.config.customer-url}"
)
public interface CustomerClient {
    @GetMapping("/verify/{customer-id}")
    boolean verifyCustomer(@PathVariable("customer-id") String customerId);
}