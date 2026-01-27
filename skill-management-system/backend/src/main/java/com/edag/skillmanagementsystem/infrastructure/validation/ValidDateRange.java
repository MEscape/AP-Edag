package com.edag.skillmanagementsystem.infrastructure.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidDateRangeValidator.class)
@Documented
public @interface ValidDateRange {

  String message() default "{project.dates.invalid}";

  String start();

  String end();

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
