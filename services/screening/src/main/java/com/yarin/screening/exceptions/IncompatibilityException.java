package com.yarin.screening.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class IncompatibilityException extends RuntimeException{
    private final String msg;
}
