package com.zb.deuggeun.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Map<String, String>> handleException(
      RuntimeException e, HttpServletRequest request) {

    HttpStatus status = HttpStatus.BAD_REQUEST;
    if (e instanceof CustomException customException) {
      status = customException.getStatusCode();
    }
    log.error("exceptionHandler 호출, {}, {}, {}", status.value(), request.getRequestURI(),
        e.getMessage());

    Map<String, String> response = getResponse(e, status);

    return ResponseEntity.status(status).body(response);

  }

  private static Map<String, String> getResponse(RuntimeException e, HttpStatus status) {
    Map<String, String> response = new HashMap<>();
    response.put("error type", status.getReasonPhrase());
    response.put("code", String.valueOf(status.value()));
    response.put("message", e.getMessage());
    return response;
  }
}
