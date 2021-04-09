package com.peter.importerservice.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = AllowedMimeTypeValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedMimeType {

  String[] value() default {};

  String message() default "Mime-Type is not allowed";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
