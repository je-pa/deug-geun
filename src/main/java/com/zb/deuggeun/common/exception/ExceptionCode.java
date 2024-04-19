package com.zb.deuggeun.common.exception;


import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {
  INVALID_TIME(BAD_REQUEST, "가능한 시간이 아닙니다."),
  RESERVATION_STATUS_NOT_MODIFIABLE(BAD_REQUEST, "예약 상태값 변경이 가능한 상태가 아닙니다."),
  IMMUTABLE_STATUS(BAD_REQUEST, "수정 가능한 상태가 아닙니다."),
  MAX_ACTIVE_PROGRAM_LIMIT_EXCEEDED(BAD_REQUEST, "활성화 프로그램의 최대 개수를 초과했습니다."),
  DATE_NOT_WITHIN_DURATION(BAD_REQUEST, "날짜가 기간에 포함되어야 합니다. "),

  // Unauthorized
  AUTHENTICATION_ISSUE(UNAUTHORIZED, "인증 이슈가 있습니다."),
  TOKEN_EXPIRED(UNAUTHORIZED, "토큰이 만료되었습니다."),
  PASSWORD_MISMATCH(UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

  RESERVATION_STATUS_PERMISSION_DENIED(FORBIDDEN, "해당 예약에 대한 권한이 없습니다."),

  // NOT_FOUND
  ENTITY_NOT_FOUND(NOT_FOUND, "개체를 찾지 못했습니다."),

  // Conflict
  LOGIN_USER_MISMATCH(CONFLICT, "로그인 유저가 일치하지 않습니다."),
  DURATION_MISMATCH(CONFLICT, "기간이 일치하지 않습니다."),
  USER_ID_CONFLICT(CONFLICT, "이미 존재하는 사용자 아이디입니다."),
  PROGRAM_DURATION_CONFLICT(CONFLICT, "중복되는 기간입니다."),
  PROGRAM_TIME_CONFLICT(CONFLICT, "중복되는 시간입니다."),
  LOCK_ACQUISITION_EXCEPTION(CONFLICT, "락 획득을 실패했습니다."),
  DURATION_SLOT_IN_ACTIVATE(CONFLICT, "활성화된 기간 슬롯이 있어 해당 작업을 진행할 수 없습니다."),
  RESERVATION_DATETIME_CONFLICT(CONFLICT, "다른 예약의 날짜와 시간이 겹칩니다."),
  RESERVATION_IN_PROGRESS(CONFLICT, "진행 중인 예약이 있어 해당 작업을 진행할 수 없습니다."),
  RESERVATION_FULL(CONFLICT, "예약 가능한 자리가 없습니다.");


  private final HttpStatus status;
  private final String message;
}
