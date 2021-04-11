package com.peter.importerservice.service.importer.validation;

import com.peter.importerservice.service.importer.ImportType;
import com.peter.importerservice.service.importer.dto.FieldConfiguration;
import com.peter.importerservice.service.importer.util.ImportTypeResolverUtils;
import com.peter.importerservice.validator.ValidationErrorCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class FieldConfigurationsValidator
    extends AbstractConstraintValidator<FieldConfigurations, List<FieldConfiguration>> {

  private final ImportTypeResolverUtils importTypeResolver;

  @Override
  public boolean isValid(List<FieldConfiguration> value, ConstraintValidatorContext context) {

    if (value.isEmpty()) {
      context
          .buildConstraintViolationWithTemplate(
              ValidationErrorCode.Import.CONFIGURATION_ERROR.name())
          .addPropertyNode(null)
          .addConstraintViolation();

      return false;
    }

    // check field configuration according to the import type
    var type = importTypeResolver.getImportTypeFromUrl();

    if (type.isEmpty()) { // cannot append because of spring pathVariable validation
      context
          .buildConstraintViolationWithTemplate(
              ValidationErrorCode.Import.CONFIGURATION_ERROR.name())
          .addPropertyNode(null)
          .addConstraintViolation();

      return false;
    }

    var result = true;
    var typ = type.get();

    result &= validateDuplicatedPosition(typ, value, context);
    result &= validateFieldsAreValid(typ, value, context);
    result &= validateRequiredFields(typ, value, context);
    result &= typ.isValid(value, context);

    return result;
  }

  boolean validateDuplicatedPosition(
          ImportType type, List<FieldConfiguration> value, ConstraintValidatorContext context) {
    var positions = value.stream().map(FieldConfiguration::getPosition).collect(Collectors.toSet());
    var noDuplicatedPosition = Objects.equals(positions.size(), value.size());

    if (!noDuplicatedPosition) {
      log.warn("Error while trying to do a {} import, some fields have the same position", type);

      context
          .buildConstraintViolationWithTemplate(
              ValidationErrorCode.Import.POSITION_USED_TWICE.name())
          .addPropertyNode(null)
          .addConstraintViolation();
    }

    return noDuplicatedPosition;
  }

  boolean validateFieldsAreValid(
      ImportType type, List<FieldConfiguration> value, ConstraintValidatorContext context) {
    var fields =
        value.stream()
            .map(FieldConfiguration::getFieldName)
            .filter(field -> type.getFieldsToImport().contains(field))
            .collect(Collectors.toSet());

    var result = Objects.equals(fields.size(), value.size());
    if (!result) {
      log.warn(
          "Error while trying to do a {} import, some fields are not valid for the current import",
          type);

      context
          .buildConstraintViolationWithTemplate(
              ValidationErrorCode.Import.INVALID_FIELD_NAME_FOR_THE_CURRENT_CONFIGURATION.name())
          .addPropertyNode(null)
          .addConstraintViolation();
    }
    return result;
  }

  boolean validateRequiredFields(
      ImportType type, List<FieldConfiguration> configuration, ConstraintValidatorContext context) {
    var requiredFields = new HashSet<>(type.getFieldsToImport());
    requiredFields.removeIf(
        f -> {
          var field = type.getField(f);
          return field.isPresent() && !field.get().isRequired();
        });

    requiredFields.removeIf(
        requiredField ->
            configuration.stream()
                .map(FieldConfiguration::getFieldName)
                .anyMatch(field -> field.equals(requiredField)));

    if (!requiredFields.isEmpty()) {

      log.warn(
          "Error while trying to do a {} import, {} {} required and missing",
          type,
          requiredFields,
          requiredFields.size() == 1 ? "is" : "are");

      context
          .buildConstraintViolationWithTemplate(
              ValidationErrorCode.Import.CONFIGURATION_ERROR.name())
          .addPropertyNode(null)
          .addConstraintViolation();
    }

    return requiredFields.isEmpty();
  }
}
