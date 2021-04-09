package com.peter.importerservice.validator;

import com.peter.importerservice.service.importer.validation.AbstractConstraintValidator;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class AllowedMimeTypeValidator
    extends AbstractConstraintValidator<AllowedMimeType, MultipartFile> {

  private AllowedMimeType declaredAnnotation;

  @Override
  public void initialize(AllowedMimeType constraintAnnotation) {
    this.declaredAnnotation = constraintAnnotation;
  }

  @Override
  public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
    if (null == file) {
      return true;
    }

    String fileMimeType = file.getContentType();
    var valid = null != fileMimeType;
    if (valid) {
      Set<MediaType> allowedMimeTypes =
          Arrays.stream(declaredAnnotation.value())
              .map(MediaType::valueOf)
              .collect(Collectors.toSet());
      valid = MediaType.valueOf(fileMimeType).isPresentIn(allowedMimeTypes);
    }

    if (!valid) {
      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate(ValidationErrorCode.Common.MIME_TYPE_ERROR.name())
          .addPropertyNode("contentType")
          .addConstraintViolation();
    }
    return valid;
  }
}
