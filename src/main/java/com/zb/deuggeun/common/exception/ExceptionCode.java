package com.zb.deuggeun.common.exception;


import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {
  IMMUTABLE_STATUS(BAD_REQUEST, "수정 가능한 상태가 아닙니다."),
  MAX_ACTIVE_PROGRAM_LIMIT_EXCEEDED(BAD_REQUEST, "활성화 프로그램의 최대 개수를 초과했습니다."),
  // Unauthorized
  AUTHENTICATION_ISSUE(UNAUTHORIZED, "인증 이슈가 있습니다."),
  TOKEN_EXPIRED(UNAUTHORIZED, "토큰이 만료되었습니다."),
  PASSWORD_MISMATCH(UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

  // NOT_FOUND
  ENTITY_NOT_FOUND(NOT_FOUND, "개체를 찾지 못했습니다."),

  // Conflict
  LOGIN_USER_MISMATCH(CONFLICT, "로그인 유저가 일치하지 않습니다."),
  USER_ID_CONFLICT(CONFLICT, "이미 존재하는 사용자 아이디입니다."),
  PROGRAM_DURATION_CONFLICT(CONFLICT, "중복되는 기간입니다.");


  private final HttpStatus status;
  private final String message;
}
