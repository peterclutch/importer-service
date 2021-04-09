package com.peter.importerservice.service.importer.validation;

import com.peter.importerservice.repository.FactoryRepository;
import com.peter.importerservice.util.SecurityUtils;
import com.peter.importerservice.validator.ValidationErrorCode;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
@AllArgsConstructor
public class FactoryNameResourceValidator implements ConstraintValidator<FactoryName, Object[]> {

  private FactoryRepository factoryRepository;

  @Override
  public boolean isValid(Object[] value, ConstraintValidatorContext context) {
    var valid = true;
    if (2 == value.length) {
      var id = (Long) value[0];
      var dto = value[1];
      if (FactoryNameConstraint.class.isInstance(dto)) {
        var brandId = SecurityUtils.getCurrentUserBrandWhenAuthorized();
        var existingName = factoryRepository.getName(id);
        var newName = ((FactoryNameConstraint) dto).getName();
        if (!existingName.equals(newName)) {
          valid = !factoryRepository.existsByNameAndBrandId(newName, brandId);
        }
      }
    } else {
      throw new RuntimeException("This validation was not implemented");
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
