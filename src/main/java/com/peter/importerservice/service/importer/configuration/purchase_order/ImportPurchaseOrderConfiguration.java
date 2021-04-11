package com.peter.importerservice.service.importer.configuration.purchase_order;

import com.peter.importerservice.service.importer.configuration.ImportConfiguration;
import com.peter.importerservice.service.importer.configuration.MultiLineConfiguration;
import com.peter.importerservice.service.importer.dto.FieldConfiguration;
import com.peter.importerservice.service.importer.dto.bo.PurchaseOrderImportBO;
import com.peter.importerservice.service.importer.dto.bo.PurchaseOrderLineImportBO;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ImportPurchaseOrderConfiguration
    implements ImportConfiguration<PurchaseOrderLineImportBO>,
        MultiLineConfiguration<PurchaseOrderLineImportBO, PurchaseOrderImportBO> {

  private static final Set<String> PERMISSIONS = Set.of("PURCHASE_ORDER_EDIT");

  @Override
  public Class getFieldsEnum() {
    return ImportPurchaseOrderFields.class;
  }

  @Override
  public Set<String> getPermissions() {
    return PERMISSIONS;
  }

  @Override
  public boolean isMultiLine() {
    return true;
  }

  @Override
  public Optional<String> convertFieldName(
      ConstraintViolation<Object> code, List<FieldConfiguration> configuration) {
    String result = null;
    var prop = code.getPropertyPath() == null ? "" : code.getPropertyPath().toString();
    if (prop.contains(".")) {
      prop = prop.substring(prop.lastIndexOf(".") + 1);
    }
    if ("productIdentifier".equalsIgnoreCase(prop)) {
      result = ImportPurchaseOrderFields.PRODUCT_IDENTIFIER.name();
    }
    return Optional.ofNullable(result);
  }
}
