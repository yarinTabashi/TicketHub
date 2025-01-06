package com.yarin.customer.exceptions;

import java.util.Map;

public record ErrorResponse(
        Map<String, String> errors
) {

}