package com.yarin.common_dtos.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class IncompatibilityException extends RuntimeException{
  private final String msg;
}