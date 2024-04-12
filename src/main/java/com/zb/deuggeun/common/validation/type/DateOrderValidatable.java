package com.zb.deuggeun.common.validation.type;

import java.time.LocalDate;

public interface DateOrderValidatable {

  LocalDate getStartDate();

  LocalDate getEndDate();

  default boolean isValidOrder() {
    return getStartDate().isBefore(getEndDate());
  }
}
