package com.peter.importerservice.service.importer.validation;

import com.peter.importerservice.repository.FactoryRepository;
import com.peter.importerservice.repository.ProductRepository;
import com.peter.importerservice.repository.PurchaseOrderRepository;
import com.peter.importerservice.service.importer.dto.bo.PurchaseOrderLineImportBO;
import com.peter.importerservice.util.SecurityUtils;
import com.peter.importerservice.util.StringUtils;
import com.peter.importerservice.validator.ValidationErrorCode;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class PurchaseOrderLineImportValidation
    implements ConstraintValidator<PurchaseOrderLineImport, PurchaseOrderLineImportBO> {

  private final PurchaseOrderRepository purchaseOrderRepository;
  private final ProductRepository productRepository;
  private final FactoryRepository factoryRepository;

  @Override
  public boolean isValid(PurchaseOrderLineImportBO value, ConstraintValidatorContext context) {
    var currentBrandId = SecurityUtils.getCurrentUserBrandWhenAuthorized();

    var isFactoryExists = true;

    if (StringUtils.isNotBlank(value.getFactoryName())) {
      isFactoryExists =
          factoryRepository.existsByNameAndBrandId(value.getFactoryName(), currentBrandId);

      if (!isFactoryExists) {
        context
            .buildConstraintViolationWithTemplate(ValidationErrorCode.Common.NOT_FOUND.name())
            .addPropertyNode("factoryName")
            .addConstraintViolation();
      }
    }

    var isProductExists = true;
    if (StringUtils.isNotBlank(value.getProduct().getProductIdentifier())) {
      isProductExists =
          productRepository.existsByBrandIdAndIdentifierValue(
              currentBrandId, value.getProduct().getProductIdentifier());

      if (!isProductExists) {
        context
            .buildConstraintViolationWithTemplate(ValidationErrorCode.Common.NOT_FOUND.name())
            .addPropertyNode("productIdentifier")
            .addConstraintViolation();
      }
    }

    var isReferenceExists =
        purchaseOrderRepository.existsByReferenceAndBrandId(value.getReference(), currentBrandId);

    if (isReferenceExists) {
      context
          .buildConstraintViolationWithTemplate(ValidationErrorCode.Common.ALREADY_EXIST.name())
          .addPropertyNode("reference")
          .addConstraintViolation();
    }

    var isValidQuantity = true;

    if (value.getProduct().getQuantity() != null && value.getProduct().getQuantity() <= 0) {

      isValidQuantity = false;

      context
          .buildConstraintViolationWithTemplate(ValidationErrorCode.Common.TOO_SHORT_VALUE.name())
          .addPropertyNode("product.quantity")
          .addConstraintViolation();
    }

    if (value.getProduct().getRawQuantity() != null) {
      isValidQuantity = false;

      context
          .buildConstraintViolationWithTemplate(ValidationErrorCode.Common.BAD_FORMAT.name())
          .addPropertyNode("product.quantity")
          .addConstraintViolation();
    }

    var isValidRawOrderDate = true;

    if (value.getRawOrderDate() != null) {
      isValidRawOrderDate = false;
      context
          .buildConstraintViolationWithTemplate(ValidationErrorCode.Common.BAD_FORMAT.name())
          .addPropertyNode("orderDate")
          .addConstraintViolation();
    }

    var isValidRawShipmentDate = true;

    if (value.getRawShipmentDate() != null) {
      isValidRawShipmentDate = false;
      context
          .buildConstraintViolationWithTemplate(ValidationErrorCode.Common.BAD_FORMAT.name())
          .addPropertyNode("shipmentDate")
          .addConstraintViolation();
    }

    return isFactoryExists
        && isProductExists
        && !isReferenceExists
        && isValidQuantity
        && isValidRawOrderDate
        && isValidRawShipmentDate;
  }
}
