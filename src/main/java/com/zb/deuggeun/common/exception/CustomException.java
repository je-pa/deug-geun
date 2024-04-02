package com.zb.deuggeun.common.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

  private final HttpStatus statusCode;

  public CustomException(HttpStatus statusCode, String message) {
    super(message);
    this.statusCode = statusCode;
  }

  public HttpStatus getStatusCode() {
    return statusCode;
  }
}
