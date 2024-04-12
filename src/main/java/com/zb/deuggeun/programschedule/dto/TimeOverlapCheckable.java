package com.zb.deuggeun.programschedule.dto;

import com.zb.deuggeun.common.validation.type.TimeOrderValidatable;
import java.util.List;

public interface TimeOverlapCheckable<T extends TimeOrderValidatable> {

  List<T> getTimeOrderValidatableList();

  default boolean hasOverlap() {
    final int H = 24;
    final int M = 60;
    boolean[] timeArray = new boolean[H * M]; // 24시간 * 60분
    List<T> timeOrderValidatableList = getTimeOrderValidatableList();

    for (TimeOrderValidatable timeSlot : timeOrderValidatableList) {
      int startMinute =
          timeSlot.getStartTime().getHour() * M + timeSlot.getStartTime().getMinute();
      int endMinute = timeSlot.getEndTime().getHour() * M + timeSlot.getEndTime().getMinute();
      for (int i = startMinute; i < endMinute; i++) {
        if (timeArray[i]) {
          return true; // 겹치는 시간대가 있으면 true 반환
        }
        timeArray[i] = true;
      }
    }
    return false; // 겹치는 시간대가 없으면 false 반환
  }
}
