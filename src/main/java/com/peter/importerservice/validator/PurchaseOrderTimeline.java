package com.peter.importerservice.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PurchaseOrderTimelineValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PurchaseOrderTimeline {
  String message() default "Purchase Order timeline is not correct.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
