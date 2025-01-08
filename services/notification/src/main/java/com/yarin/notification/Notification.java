package com.yarin.notification;

import java.time.LocalDateTime;
import com.yarin.common_dtos.Customer;
import com.yarin.common_dtos.Order;
import com.yarin.notification.dtos.DeliveryStatus;
import com.yarin.notification.dtos.NotificationReason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document
public class Notification {
    @Id
    private Long id;
    private Customer customer;
    private NotificationReason notificationReason;
    private DeliveryStatus deliveryStatus; // for tracking
    private Order order; // Optional
    private LocalDateTime sentAt;
}