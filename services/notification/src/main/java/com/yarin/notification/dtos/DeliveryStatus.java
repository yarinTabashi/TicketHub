package com.yarin.notification.dtos;

public enum DeliveryStatus {
    PENDING, // The notification is in the queue
    IN_PROGRESS,
    SENT, // The notification has been successfully sent
    FAILED,
}