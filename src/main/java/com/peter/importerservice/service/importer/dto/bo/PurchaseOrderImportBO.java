package com.peter.importerservice.service.importer.dto.bo;

import com.peter.importerservice.service.importer.configuration.MultiLineElement;
import com.peter.importerservice.service.importer.validation.PurchaseOrderImport;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@PurchaseOrderImport
public class PurchaseOrderImportBO implements MultiLineElement<PurchaseOrderLineImportBO> {

  private Long lineNumber;

  private List<PurchaseOrderLineImportBO> lines = new ArrayList<>();

  @Override
  public void addLine(PurchaseOrderLineImportBO object) {
    this.lines.add(object);
  }
}
