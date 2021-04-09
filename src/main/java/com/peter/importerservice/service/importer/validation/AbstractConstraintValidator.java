package com.peter.importerservice.service.importer.validation;

import javax.validation.ConstraintValidator;
import java.lang.annotation.Annotation;

import static java.util.Objects.isNull;

public abstract class AbstractConstraintValidator<A extends Annotation, T>
    implements ConstraintValidator<A, T> {

  protected boolean nullOrEmpty(Object value) {
    return isNull(value) || "".equals(value);
  }
}
