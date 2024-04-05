package com.zb.deuggeun.common.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {
  IMMUTABLE_STATUS(HttpStatus.BAD_REQUEST, ExceptionMessage.IMMUTABLE_STATUS),
  MAX_ACTIVE_PROGRAM_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST,
      ExceptionMessage.MAX_ACTIVE_PROGRAM_LIMIT_EXCEEDED),
  // Unauthorized
  AUTHENTICATION_ISSUE(HttpStatus.UNAUTHORIZED, ExceptionMessage.AUTHENTICATION_ISSUE),
  TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, ExceptionMessage.TOKEN_EXPIRED),
  PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, ExceptionMessage.PASSWORD_MISMATCH),

  // NOT_FOUND
  ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, ExceptionMessage.ENTITY_NOT_FOUND),

  // Conflict
  LOGIN_USER_MISMATCH(HttpStatus.CONFLICT, ExceptionMessage.LOGIN_USER_MISMATCH),
  USER_ID_CONFLICT(HttpStatus.CONFLICT, ExceptionMessage.USER_ID_CONFLICT);


  private final HttpStatus status;
  private final String message;
}
