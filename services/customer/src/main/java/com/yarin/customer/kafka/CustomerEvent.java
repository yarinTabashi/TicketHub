package com.yarin.customer.kafka;

public record CustomerEvent(String customerId, String email, String fullName, CustomerEventType type) {
    // Enum to define the type of event
    public enum CustomerEventType {
        REGISTERED,
        UPDATED,
        DELETED
    }
}