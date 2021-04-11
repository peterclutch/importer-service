package com.peter.importerservice.service.importer.configuration.purchase_order;

import com.peter.importerservice.service.importer.configuration.ImportConfigurationFields;
import com.peter.importerservice.service.importer.dto.bo.PurchaseOrderLineImportBO;
import com.peter.importerservice.util.StringUtils;

import java.time.LocalDate;
import java.util.function.BiConsumer;

public enum ImportPurchaseOrderFields implements ImportConfigurationFields<PurchaseOrderLineImportBO> {
  REFERENCE(ImportPurchaseOrderFields::setReference),
  FACTORY_NAME(ImportPurchaseOrderFields::setFactoryName),
  PRODUCT_IDENTIFIER(ImportPurchaseOrderFields::setProductIdentifier),
  QUANTITY(ImportPurchaseOrderFields::setQuantity),
  UNIT(ImportPurchaseOrderFields::setUnit),
  SHIPMENT_DATE(ImportPurchaseOrderFields::setShipmentDate) {
    @Override
    public boolean isRequired() {
      return false;
    }
  },
  ORDER_DATE(ImportPurchaseOrderFields::setOrderDate) {
    @Override
    public boolean isRequired() {
      return false;
    }
  };

  private static final double validQuantiy = 1D;

  private final BiConsumer<PurchaseOrderLineImportBO, String> consumer;

  ImportPurchaseOrderFields(BiConsumer<PurchaseOrderLineImportBO, String> consumer) {
    this.consumer = consumer;
  }

  @Override
  public void setValue(PurchaseOrderLineImportBO bo, String value) {
    this.consumer.accept(bo, value);
  }

  private static void setReference(PurchaseOrderLineImportBO bo, String value) {
    bo.setReference(value);
  }

  private static void setFactoryName(PurchaseOrderLineImportBO bo, String value) {
    bo.setFactoryName(value);
  }

  private static void setProductIdentifier(PurchaseOrderLineImportBO bo, String value) {
    bo.getProduct().setProductIdentifier(value);
  }

  private static void setQuantity(PurchaseOrderLineImportBO bo, String value) {
    if (value != null) {
      try {
        bo.getProduct().setQuantity(Double.valueOf(value));
      } catch (Exception e) {
        bo.getProduct()
            .setQuantity(
                validQuantiy); // Set random valid value to avoid a empty exception for javax
        // validation
        bo.getProduct().setRawQuantity(value);
      }
    }
  }

  private static void setUnit(PurchaseOrderLineImportBO bo, String value) {
    bo.getProduct().setUnit(value);
  }

  private static void setShipmentDate(PurchaseOrderLineImportBO bo, String value) {
    if (StringUtils.isNotBlank(value)) {
      try {
        bo.setShipmentDate(LocalDate.parse(value));
      } catch (Exception e) {
        bo.setRawShipmentDate(value);
      }
    }
  }

  private static void setOrderDate(PurchaseOrderLineImportBO bo, String value) {
    if (StringUtils.isNotBlank(value)) {
      try {
        bo.setOrderDate(LocalDate.parse(value));
      } catch (Exception e) {
        bo.setRawOrderDate(value);
      }
    }
  }
}
