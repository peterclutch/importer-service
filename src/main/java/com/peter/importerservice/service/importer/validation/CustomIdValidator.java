package com.peter.importerservice.service.importer.validation;

import com.peter.importerservice.repository.FactoryRepository;
import com.peter.importerservice.util.SecurityUtils;
import com.peter.importerservice.validator.ValidationErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
@Transactional
public class CustomIdValidator implements ConstraintValidator<CustomId, CustomIdConstraint> {

  private final FactoryRepository factoryRepository;

  @Override
  public boolean isValid(CustomIdConstraint value, ConstraintValidatorContext context) {
    var valid = true;
    if (null == value || null == value.getCustomId()) {
      return valid;
    }

    var brandId = SecurityUtils.getCurrentUserBrandWhenAuthorized();
    var factoryId = value.getEntityId();
    var newCustomId = value.getCustomId();

    if (null == factoryId) {
      valid = !factoryRepository.existsByCustomIdAndBrandId(newCustomId, brandId);
    } else {
      var existingCustomId = factoryRepository.getCustomId(factoryId, brandId);
      if (!newCustomId.equals(existingCustomId)) {
        valid = !factoryRepository.existsByCustomIdAndBrandId(newCustomId, brandId);
      }
    }

    if (!valid) {
      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate(ValidationErrorCode.Factory.CUSTOM_ID.name())
          .addPropertyNode("customId")
          .addConstraintViolation();
    }

    return valid;
  }
}
