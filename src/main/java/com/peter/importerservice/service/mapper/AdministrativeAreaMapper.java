package com.peter.importerservice.service.mapper;

import com.peter.importerservice.domain.AdministrativeArea;
import com.peter.importerservice.service.dto.AdministrativeAreaDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdministrativeAreaMapper
        extends AbstractEntityMapper<AdministrativeAreaDTO, AdministrativeArea> {

    AdministrativeAreaDTO toDto(AdministrativeArea administrativeArea);

    AdministrativeAreaDTO toDto(String isoCode);

    AdministrativeArea toEntity(String isoCode);

    default String toIsoCode(AdministrativeAreaDTO area) {
        if (area == null) {
            return null;
        }
        return area.getIsoCode();
    }

    default String toIsoCode(AdministrativeArea area) {
        if (area == null) {
            return null;
        }
        return area.getIsoCode();
    }
}
