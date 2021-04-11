package com.peter.importerservice.service.mapper;

import com.peter.importerservice.domain.PurchaseOrder;
import com.peter.importerservice.domain.PurchaseOrderProduct;
import com.peter.importerservice.service.importer.dto.bo.PurchaseOrderImportBO;
import com.peter.importerservice.service.importer.dto.bo.PurchaseOrderLineImportBO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
        componentModel = "spring")
@DecoratedWith(PurchaseOrderMapperDecorator.class)
public interface PurchaseOrderMapper {

    @Named("importer")
    PurchaseOrder toEntity(PurchaseOrderImportBO bo);

    @Named("importerLine")
    PurchaseOrder toEntity(PurchaseOrderLineImportBO firstElement);

    @Named("importerLine")
    @Mapping(source = "product.quantity", target = "quantity")
    @Mapping(source = "product.unit", target = "unit")
    @Mapping(source = "product.productIdentifier", target = "product.identifierValue")
    PurchaseOrderProduct toLineEntity(PurchaseOrderLineImportBO bo);
}
