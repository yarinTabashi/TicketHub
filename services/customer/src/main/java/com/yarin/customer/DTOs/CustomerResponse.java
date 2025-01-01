package com.yarin.customer.DTOs;

import com.yarin.customer.Customer.Address;

public record CustomerResponse(
        String id,
        String firstname,
        String lastname,
        String email,
        Address address
) {

}