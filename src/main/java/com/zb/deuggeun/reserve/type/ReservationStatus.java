package com.zb.deuggeun.reserve.type;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ReservationStatus {
  CREATED("생성"),
  APPROVED("승인"),
  REJECTED("거절"),
  COMPLETED("완료"),
  CANCELED("취소");

  private final String description; // 상태 설명
}
