package com.peter.importerservice.service.importer.validation;

import com.peter.importerservice.repository.FactoryRepository;
import com.peter.importerservice.util.SecurityUtils;
import com.peter.importerservice.util.StringUtils;
import com.peter.importerservice.validator.ValidationErrorCode;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class FactoryNameValidator implements ConstraintValidator<FactoryName, FactoryNameConstraint> {

  private final FactoryRepository factoryRepository;

  @Override
  public boolean isValid(FactoryNameConstraint value, ConstraintValidatorContext context) {
    if (null == value || StringUtils.isBlank(value.getName())) {
      return true;
    }
    var valid = true;
    var brandId = SecurityUtils.getCurrentUserBrandWhenAuthorized();
    var id = value.getId();

    if (null == id) {
      valid = !factoryRepository.existsByNameAndBrandId(value.getName(), brandId);
    } else {
      var existingName = factoryRepository.getName(id);
      var newName = value.getName();
      if (existingName == null || !existingName.equals(newName)) {
        valid = !factoryRepository.existsByNameAndBrandId(newName, brandId);
      }
    }

    if (!valid) {
      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate(ValidationErrorCode.Factory.FACTORY_NAME.name())
          .addPropertyNode("name")
          .addConstraintViolation();
    }

    return valid;
  }
}
