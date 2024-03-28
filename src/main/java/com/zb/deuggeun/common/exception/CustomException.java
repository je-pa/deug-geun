package com.zb.deuggeun.common.exception;

public class CustomException extends RuntimeException {

  private final int statusCode;

  public CustomException(int statusCode, String message) {
    super(message);
    this.statusCode = statusCode;
  }

  public int getStatusCode() {
    return statusCode;
  }
}
