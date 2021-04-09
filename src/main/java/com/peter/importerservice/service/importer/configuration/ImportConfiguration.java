package com.peter.importerservice.service.importer.configuration;

import com.peter.importerservice.service.importer.dto.FieldConfiguration;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public interface ImportConfiguration<T extends LineElement> {

  default T createBO() {
    try {
      return (T) getBoClass().getConstructor().newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  private Class getBoClass() {
    try {
      var opt =
          Stream.of(getClass().getGenericInterfaces())
              .map(type -> (ParameterizedType) type)
              .filter(
                  type ->
                      type.getRawType().getTypeName().equals(ImportConfiguration.class.getName()))
              .findFirst()
              .orElseThrow(
                  () ->
                      new RuntimeException(
                          "INTERNAL_ERROR", new Exception()));
      return Class.forName(opt.getActualTypeArguments()[0].getTypeName());
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  Class getFieldsEnum();

  Set<String> getPermissions();

  default boolean isValid(
          List<FieldConfiguration> configuration, ConstraintValidatorContext context) {
    return true;
  }

  default Optional<String> convertFieldName(
      ConstraintViolation<Object> code, List<FieldConfiguration> configuration) {
    return Optional.empty();
  }

  default boolean isMultiLine() {
    return false;
  }
}
