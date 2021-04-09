package com.peter.importerservice.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = StringLengthValidator.class)
public @interface StringLength {

  String message() default "{javax.validation.constraints.Size.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  /** @return size the element must be higher or equal to */
  int min() default 0;

  /** @return size the element must be lower or equal to */
  int max() default Integer.MAX_VALUE;
}
