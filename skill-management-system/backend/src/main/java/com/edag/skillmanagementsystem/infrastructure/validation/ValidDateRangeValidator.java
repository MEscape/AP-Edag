package com.edag.skillmanagementsystem.infrastructure.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDate;

public class ValidDateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

  private String startField;
  private String endField;

  @Override
  public void initialize(ValidDateRange annotation) {
    this.startField = annotation.start();
    this.endField = annotation.end();
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    try {
      Field start = value.getClass().getDeclaredField(startField);
      Field end = value.getClass().getDeclaredField(endField);

      start.setAccessible(true);
      end.setAccessible(true);

      LocalDate startDate = (LocalDate) start.get(value);
      LocalDate endDate = (LocalDate) end.get(value);

      if (startDate == null || endDate == null) {
        return true; // nothing to validate
      }

      return !startDate.isAfter(endDate);

    } catch (Exception ignored) {
      return true;
    }
  }
}
