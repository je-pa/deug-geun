package com.zb.deuggeun.member.type;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ProfileVisibility {
  ACTIVE("프로필 공개"),
  INACTIVE("프로필 비공개");
  private final String name;
}
