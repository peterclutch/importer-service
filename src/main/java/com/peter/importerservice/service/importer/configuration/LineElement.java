package com.peter.importerservice.service.importer.configuration;

public interface LineElement<T> {

  Long getLineNumber();

  void setLineNumber(Long lineNumber);
}
