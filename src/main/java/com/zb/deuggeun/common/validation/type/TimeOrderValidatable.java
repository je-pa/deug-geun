package com.zb.deuggeun.common.validation.type;

import java.time.LocalTime;

public interface TimeOrderValidatable {

  LocalTime getStartTime();

  LocalTime getEndTime();

  default boolean isValidOrder() {
    return getStartTime().isBefore(getEndTime());
  }
}
