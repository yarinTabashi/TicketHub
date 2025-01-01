package com.yarin.order.customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "customer-url",
        url = "${application.config.customer-url}"
)
public interface CustomerClient {
    @GetMapping("/exists/{customer-id}")
    boolean isExist(@PathVariable("customer-id") String customerId);
}