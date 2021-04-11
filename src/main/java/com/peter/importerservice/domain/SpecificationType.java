package com.peter.importerservice.domain;

import com.peter.importerservice.validator.GtinValidation;
import com.peter.importerservice.validator.ValidationErrorCode;
import lombok.Getter;

import javax.validation.ConstraintValidatorContext;

@Getter
public enum SpecificationType {
  SKU(true),
  GTIN(true) {
    @Override
    public boolean validate(Object node, ConstraintValidatorContext context) {
      return node == null || super.validate(node, context);
    }

    @Override
    public boolean validate(String value, ConstraintValidatorContext context) {
      return value == null || new GtinValidation().isValid(value, context);
    }
  },
  REFERENCE(true),
  DESCRIPTION(false) {
    @Override
    public boolean validate(String value, ConstraintValidatorContext context) {
      var isValid = null != value && value.length() <= MAX_DESCRIPTION_SIZE;
      if (!isValid)
        context
            .buildConstraintViolationWithTemplate(ValidationErrorCode.Common.TOO_LONG_VALUE.name())
            .addPropertyNode(this.name().toLowerCase())
            .addConstraintViolation();
      return isValid;
    }
  },
  NAME(true);

  private static final int MAX_DESCRIPTION_SIZE = 500;

  private static final int MAX_SPECIFICATION_TEXT_SIZE = 50;

  private final boolean identifier;

  SpecificationType(boolean identifier) {
    this.identifier = identifier;
  }

  public boolean validate(Object node, ConstraintValidatorContext context) {
    return node instanceof String && validate(node.toString(), context);
  }

  public boolean validate(String value, ConstraintValidatorContext context) {
    var isValid = null == value || value.length() <= MAX_SPECIFICATION_TEXT_SIZE;
    if (!isValid)
      context
          .buildConstraintViolationWithTemplate(ValidationErrorCode.Common.TOO_LONG_VALUE.name())
          .addPropertyNode(this.name().toLowerCase())
          .addConstraintViolation();
    return isValid;
  }
}
