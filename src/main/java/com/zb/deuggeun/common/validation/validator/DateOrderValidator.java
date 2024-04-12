package com.zb.deuggeun.common.validation.validator;

import com.zb.deuggeun.common.validation.type.DateOrderValidatable;
import com.zb.deuggeun.common.validation.anotaion.DateOrder;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateOrderValidator implements ConstraintValidator<DateOrder, DateOrderValidatable> {

  @Override
  public boolean isValid(DateOrderValidatable validatable, ConstraintValidatorContext context) {
    if (validatable == null) {
      return true;
    }
    return validatable.getStartDate().isBefore(validatable.getEndDate());
  }
}
