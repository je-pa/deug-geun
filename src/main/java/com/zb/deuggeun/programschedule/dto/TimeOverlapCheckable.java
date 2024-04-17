package com.zb.deuggeun.programschedule.dto;

import com.zb.deuggeun.common.validation.type.TimeOrderValidatable;
import java.util.Comparator;
import java.util.List;

public interface TimeOverlapCheckable<T extends TimeOrderValidatable> {

  List<T> getTimeOrderValidatableList();

  default boolean hasOverlap() {
    List<T> timeOrderValidatableList = getTimeOrderValidatableList();

    // 시간대를 시작 시간을 기준으로 정렬
    timeOrderValidatableList.sort(Comparator.comparing(t -> t.getStartTime()));

    // 시간대를 순회하면서 겹치는지 확인
    for (int i = 0; i < timeOrderValidatableList.size() - 1; i++) {
      T firstTimeSlot = timeOrderValidatableList.get(i);
      T secondTimeSlot = timeOrderValidatableList.get(i + 1);

      // 첫 번째 시간대의 종료 시간과 두 번째 시간대의 시작 시간을 비교하여 겹치는지 확인
      if (firstTimeSlot.getEndTime().isAfter(secondTimeSlot.getStartTime())) {
        return true; // 겹치는 시간대가 있으면 true 반환
      }
    }
    return false; // 겹치는 시간대가 없으면 false 반환
  }
}
