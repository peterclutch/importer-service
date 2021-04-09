package com.peter.importerservice.service.importer.validation;

import javax.validation.Constraint;
import javax.validation.ConstraintTarget;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = {CustomIdValidator.class, CustomIdResourceValidator.class})
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomId {

  String message() default "Custom ID is not unique";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  ConstraintTarget validationAppliesTo() default ConstraintTarget.IMPLICIT;
}
