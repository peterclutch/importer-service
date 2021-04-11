package com.peter.importerservice.service.mapper;

import com.peter.importerservice.domain.Brand;
import com.peter.importerservice.domain.Factory;
import com.peter.importerservice.domain.PurchaseOrder;
import com.peter.importerservice.service.importer.dto.bo.PurchaseOrderImportBO;
import com.peter.importerservice.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

public abstract class PurchaseOrderMapperDecorator implements PurchaseOrderMapper {

    @Autowired
    private PurchaseOrderMapper delegate;

    @Override
    public PurchaseOrder toEntity(PurchaseOrderImportBO bo) {
        var firstElement = bo.getLines().get(0);
        PurchaseOrder entity = delegate.toEntity(firstElement);

        entity.setPurchaseOrderProducts(
                bo.getLines().stream()
                        .map(line -> delegate.toLineEntity(line))
                        .collect(Collectors.toSet()));

        entity.setBrand(new Brand().id(SecurityUtils.getCurrentUserBrandWhenAuthorized()));
        return entity;
    }
}
