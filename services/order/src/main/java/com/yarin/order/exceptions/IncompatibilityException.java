package com.yarin.order.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class IncompatibilityException extends RuntimeException{
  private final String msg;
}