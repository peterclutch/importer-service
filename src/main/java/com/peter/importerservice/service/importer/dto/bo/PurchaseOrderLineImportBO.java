package com.peter.importerservice.service.importer.dto.bo;

import com.peter.importerservice.service.importer.configuration.LineElement;
import com.peter.importerservice.service.importer.validation.PurchaseOrderLineImport;
import com.peter.importerservice.validator.ITimelineConstraint;
import com.peter.importerservice.validator.PurchaseOrderTimeline;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@PurchaseOrderTimeline
@PurchaseOrderLineImport
public class PurchaseOrderLineImportBO
    implements ITimelineConstraint, LineElement<PurchaseOrderLineImportBO> {

  @NotBlank private String reference;

  private LocalDate orderDate;
  private String rawOrderDate;
  private LocalDate shipmentDate;
  private String rawShipmentDate;

  @NotBlank private String factoryName;

  @Valid private PurchaseOrderProductImportBO product = new PurchaseOrderProductImportBO();

  private Long lineNumber;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (reference == null) {
      return false;
    }
    PurchaseOrderLineImportBO that = (PurchaseOrderLineImportBO) o;
    return reference.equals(that.reference);
  }

  @Getter
  @Setter
  public class PurchaseOrderProductImportBO implements LineElement {
    @NotBlank private String productIdentifier;
    @NotNull private Double quantity;
    private String rawQuantity;
    @NotBlank private String unit;

    private Long lineNumber;
  }
}
