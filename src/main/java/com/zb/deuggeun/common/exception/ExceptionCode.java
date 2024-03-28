package com.zb.deuggeun.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {
  // Unauthorized
  AUTHENTICATION_ISSUE(401, "인증 이슈가 있습니다."),
  TOKEN_EXPIRED(401, "토큰이 만료되었습니다."),
  PASSWORD_MISMATCH(401, "비밀번호가 일치하지 않습니다."),

  // NOT_FOUND
  ENTITY_NOT_FOUND(404, "개체를 찾지 못했습니다."),

  // Conflict
  USER_ID_CONFLICT(409, "이미 존재하는 사용자 아이디입니다.");


  private final int status;
  private final String message;
}
