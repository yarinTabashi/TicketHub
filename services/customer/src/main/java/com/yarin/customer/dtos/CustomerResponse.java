package com.yarin.customer.dtos;

public record CustomerResponse(
        String id,
        String firstname,
        String lastname,
        String email,
        String city
) {

}