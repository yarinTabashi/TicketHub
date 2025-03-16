package com.yarin.customer.kafka;

public record UserRegisteredEvent(String customerId, String email, String fullName){}