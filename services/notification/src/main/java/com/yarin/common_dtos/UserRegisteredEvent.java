package com.yarin.common_dtos;

public record UserRegisteredEvent(String customerId, String email, String fullName){}