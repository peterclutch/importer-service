package com.peter.importerservice.service.importer.configuration;

import java.lang.reflect.ParameterizedType;
import java.util.stream.Stream;

public interface MultiLineConfiguration<E, T extends MultiLineElement> {

  default T createMultiLineBO() {
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
                      type.getRawType()
                          .getTypeName()
                          .equals(MultiLineConfiguration.class.getName()))
              .findFirst()
              .orElseThrow(
                  () -> new RuntimeException("INTERNAL_ERROR"));
      return Class.forName(opt.getActualTypeArguments()[1].getTypeName());
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
