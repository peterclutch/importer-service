package com.peter.importerservice.service.importer.validation;

import com.peter.importerservice.repository.FactoryRepository;
import com.peter.importerservice.util.SecurityUtils;
import com.peter.importerservice.validator.ValidationErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
@SupportedValidationTarget(ValidationTarget.PARAMETERS)
@AllArgsConstructor
public class CustomIdResourceValidator implements ConstraintValidator<CustomId, Object[]> {

  private final FactoryRepository factoryRepository;

  @Override
  public boolean isValid(Object[] value, ConstraintValidatorContext context) {
    var valid = true;

    if (2 == value.length) {
      var id = (Long) value[0];
      var dto = value[1];
      if (CustomIdConstraint.class.isInstance(dto)) {
        var brandId = SecurityUtils.getCurrentUserBrandWhenAuthorized();
        var existingCustomId = factoryRepository.getCustomId(id, brandId);
        var newCustomId = ((CustomIdConstraint) dto).getCustomId();

        if (null != newCustomId && !newCustomId.equals(existingCustomId)) {
          valid = !factoryRepository.existsByCustomIdAndBrandId(newCustomId, brandId);
        }
      }
    } else {
      throw new RuntimeException("This validation was not implemented");
    }

    if (!valid) {
      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate(ValidationErrorCode.Factory.CUSTOM_ID.name())
          .addPropertyNode("registrationNumber")
          .addConstraintViolation();
    }

    return valid;
  }
}
