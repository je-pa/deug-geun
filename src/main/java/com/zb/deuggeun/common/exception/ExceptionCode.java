package com.zb.deuggeun.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {
  // Unauthorized
  AUTHENTICATION_ISSUE(HttpStatus.UNAUTHORIZED, ExceptionMessage.AUTHENTICATION_ISSUE),
  TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, ExceptionMessage.TOKEN_EXPIRED),
  PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, ExceptionMessage.PASSWORD_MISMATCH),

  // NOT_FOUND
  ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, ExceptionMessage.ENTITY_NOT_FOUND),

  // Conflict
  USER_ID_CONFLICT(HttpStatus.CONFLICT, ExceptionMessage.USER_ID_CONFLICT);


  private final HttpStatus status;
  private final String message;
}
