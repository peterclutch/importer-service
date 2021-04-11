package com.peter.importerservice.service.importer.validation;

import com.peter.importerservice.service.importer.dto.bo.PurchaseOrderImportBO;
import com.peter.importerservice.service.importer.dto.bo.PurchaseOrderLineImportBO;
import com.peter.importerservice.validator.ValidationErrorCode;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * we assume that the po reference is the same because it's checked before and there is also a least
 * one element in the list
 */
@AllArgsConstructor
public class PurchaseOrderImportValidator
    implements ConstraintValidator<PurchaseOrderImport, PurchaseOrderImportBO> {

  @Override
  public boolean isValid(PurchaseOrderImportBO value, ConstraintValidatorContext context) {

    boolean isValid = true;

    PurchaseOrderLineImportBO firstElement = null;
    for (var line : value.getLines()) {

      if (firstElement == null) {
        firstElement = line;
      } else {
        boolean sameFactoryName =
            Objects.equals(firstElement.getFactoryName(), line.getFactoryName());

        if (!sameFactoryName) {
          context
              .buildConstraintViolationWithTemplate(
                  ValidationErrorCode.Import.PurchaseOrder.INCONSISTENT_FACTORY_NAME.name())
              .addPropertyNode("factoryName")
              .addConstraintViolation();
        }

        boolean sameOrderDate = Objects.equals(firstElement.getOrderDate(), line.getOrderDate());

        if (!sameOrderDate) {
          context
              .buildConstraintViolationWithTemplate(
                  ValidationErrorCode.Import.PurchaseOrder.INCONSISTENT_ORDER_DATE.name())
              .addPropertyNode("orderDate")
              .addConstraintViolation();
        }

        boolean sameShipmentDate =
            Objects.equals(firstElement.getShipmentDate(), line.getShipmentDate());

        if (!sameShipmentDate) {
          context
              .buildConstraintViolationWithTemplate(
                  ValidationErrorCode.Import.PurchaseOrder.INCONSISTENT_SHIPMENT_DATE.name())
              .addPropertyNode("shipmentDate")
              .addConstraintViolation();
        }

        isValid &= (sameFactoryName && sameOrderDate && sameShipmentDate);
      }
    }

    return isValid;
  }
}
