package com.zb.deuggeun.program.type;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ProgramStatus {
  CREATED("생성완료"),
  ACTIVE("활성화"),
  INACTIVE("비활성화"),
  DELETED("삭제");

  private final String name;
}
