package com.peter.importerservice.validator;

import com.peter.importerservice.domain.SpecificationType;
import com.peter.importerservice.service.importer.validation.AbstractConstraintValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidatorContext;

import static java.lang.Character.getNumericValue;

public class GtinValidation extends AbstractConstraintValidator<Gtin, Object> {

  private static final Logger LOGGER = LoggerFactory.getLogger(GtinValidation.class);

  @Override
  public void initialize(Gtin constraintAnnotation) {
    // No need to initialise anything here.
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    var emptyOrNull = nullOrEmpty(value);
    if (!emptyOrNull) {
      var isGtinValid = isGtinCompliant(value);
      if (!isGtinValid) {
        context
            .buildConstraintViolationWithTemplate(ValidationErrorCode.Product.INVALID_GTIN.name())
            .addPropertyNode(null)
            .inIterable()
            .atKey(SpecificationType.GTIN.name())
            .addConstraintViolation();
        return false;
      }
    }
    return true;
  }

  private boolean isGtinCompliant(Object value) {
    return (value instanceof String) && isGtinValid((String) value);
  }

  private boolean isGtinValid(final String gtin) {
    try {
      int lastIndex = gtin.length() - 1;
      char[] gtinArray = gtin.toCharArray();
      int gtinLastNumber = validateAndGetNumber(gtinArray[lastIndex], gtin);
      int gtinSum = getGtinSum(gtinArray, gtin);
      int ceiling = (int) (Math.ceil((double) gtinSum / 10D) * 10D);
      return (ceiling - gtinSum) == gtinLastNumber;
    } catch (NumberFormatException e) {
      LOGGER.error("Wrong format", e);
    } catch (Exception e) {
      LOGGER.error("An unexpected error has occurred", e);
    }
    return false;
  }

  private int validateAndGetNumber(char character, String gtin) {
    if (isDigit(character)) {
      return getNumericValue(character);
    }

    throw new NumberFormatException(
        String.format("%s gtin is invalid due to character '%s'", gtin, character));
  }

  private static boolean isDigit(char character) {
    return character >= '0' && character <= '9';
  }

  private int getGtinSum(char[] gtinArray, String gtin) {
    int gtinSum = 0;
    boolean flag = true;
    for (int index = gtinArray.length - 2; index >= 0; index--) {
      char gtinCharacter = gtinArray[index];
      int number = validateAndGetNumber(gtinCharacter, gtin);
      flag = !flag;
      gtinSum += (flag ? number : number * 3);
    }
    return gtinSum;
  }
}
