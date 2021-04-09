package com.peter.importerservice.validator;

import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.invoke.MethodHandles;

/**
 * !!!!!! COPY OF
 * https://github.com/hibernate/hibernate-validator/blob/master/engine/src/main/java/org/hibernate/validator/internal/constraintvalidators/bv/size/SizeValidatorForCharSequence.java#L28
 * !!!!!! Check that the length of a character sequence is between min and max.
 *
 * @author Emmanuel Bernard
 * @author Gavin King
 * @author Hardy Ferentschik
 */
public class StringLengthValidator implements ConstraintValidator<StringLength, CharSequence> {

  private static final Log LOG = LoggerFactory.make(MethodHandles.lookup());

  private int min;
  private int max;

  public void initialize(StringLength parameters) {
    min = parameters.min();
    max = parameters.max();
    validateParameters();
  }

  @Override
  public boolean isValid(CharSequence charSequence, ConstraintValidatorContext context) {
    if (charSequence == null) {
      return true;
    }
    int length = charSequence.length();
    if (length < min) {
      context
          .buildConstraintViolationWithTemplate(ValidationErrorCode.Common.TOO_SHORT_VALUE.name())
          // .addPropertyNode(context.get)
          .addConstraintViolation();
      return false;
    } else if (length > max) {
      context
          .buildConstraintViolationWithTemplate(ValidationErrorCode.Common.TOO_LONG_VALUE.name())
          // .addPropertyNode(this.name().toLowerCase())
          .addConstraintViolation();
      return false;
    }
    return true;
  }

  private void validateParameters() {
    if (min < 0) {
      throw LOG.getMinCannotBeNegativeException();
    }
    if (max < 0) {
      throw LOG.getMaxCannotBeNegativeException();
    }
    if (max < min) {
      throw LOG.getLengthCannotBeNegativeException();
    }
  }
}
