package com.peter.importerservice.service.mapper;

import com.peter.importerservice.domain.BrandFactory;
import com.peter.importerservice.domain.Factory;
import com.peter.importerservice.service.bo.FactoryBO;
import com.peter.importerservice.service.importer.dto.FactoryImportBO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
        componentModel = "spring",
        uses = {
                AddressMapper.class,
                ContactMapper.class,
                AdministrativeAreaMapper.class,
                CountryMapper.class
        })
@DecoratedWith(FactoryMapperDecorator.class)
public interface FactoryMapper {

    Factory toFactory(FactoryBO entityBO);

    @Mapping(source = "id", target = "id.factory.id")
    BrandFactory toBrandFactory(FactoryBO bo);

    @Named("business")
    FactoryBO toBO(Factory factory);

    FactoryBO toBO(FactoryImportBO factory);
}
