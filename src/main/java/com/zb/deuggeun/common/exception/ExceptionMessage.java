package com.zb.deuggeun.common.exception;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessage {

  public static final String IMMUTABLE_STATUS = "수정 가능한 상태가 아닙니다.";
  public static final String MAX_ACTIVE_PROGRAM_LIMIT_EXCEEDED = "활성화 프로그램의 최대 개수를 초과했습니다.";
  public static final String AUTHENTICATION_ISSUE = "인증 이슈가 있습니다.";
  public static final String TOKEN_EXPIRED = "토큰이 만료되었습니다.";
  public static final String PASSWORD_MISMATCH = "비밀번호가 일치하지 않습니다.";
  public static final String ENTITY_NOT_FOUND = "개체를 찾지 못했습니다.";
  public static final String LOGIN_USER_MISMATCH = "로그인 유저가 일치하지 않습니다.";
  public static final String USER_ID_CONFLICT = "이미 존재하는 사용자 아이디입니다.";
}
