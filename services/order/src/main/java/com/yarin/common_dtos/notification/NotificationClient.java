package com.yarin.common_dtos.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "notification-url",
        url = "${application.config.notification-url}"
)
public interface NotificationClient {
    @GetMapping("/verify/{customer-id}")
    boolean sendOrderSucceedNotification(@PathVariable("notification-content") String notificationContent);
}