package com.peter.importerservice.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = GtinValidation.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Gtin {

  String message() default "This field is not GTIN compliant.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
