package com.peter.importerservice.service.importer.configuration;

public interface ImportConfigurationFields<T> {

  void setValue(T bo, String value);

  default boolean isRequired() {
    return true;
  }
}
