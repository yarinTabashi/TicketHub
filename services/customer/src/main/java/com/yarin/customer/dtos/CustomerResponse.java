package com.yarin.customer.dtos;

import com.yarin.customer.customer.Address;

public record CustomerResponse(
        String id,
        String firstname,
        String lastname,
        String email,
        Address address
) {

}