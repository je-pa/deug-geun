package com.zb.deuggeun.member.type;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Role {
  ROLE_MEMBER("회원"),
  ROLE_TRAINER("트레이너");

  private final String roleName;
}
