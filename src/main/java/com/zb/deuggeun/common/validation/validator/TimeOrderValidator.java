package com.zb.deuggeun.common.validation.validator;

import com.zb.deuggeun.common.validation.anotaion.TimeOrder;
import com.zb.deuggeun.common.validation.type.TimeOrderValidatable;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TimeOrderValidator implements ConstraintValidator<TimeOrder, TimeOrderValidatable> {

  @Override
  public boolean isValid(TimeOrderValidatable validatable, ConstraintValidatorContext context) {
    if (validatable == null) {
      return true;
    }
    return validatable.isValidOrder();
  }
}
