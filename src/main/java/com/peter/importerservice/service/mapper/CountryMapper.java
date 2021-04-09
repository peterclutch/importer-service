package com.peter.importerservice.service.mapper;

import com.peter.importerservice.domain.Country;
import com.peter.importerservice.service.dto.CountryDTO;
import org.mapstruct.Mapper;

import java.util.SortedSet;

@Mapper(componentModel = "spring")
public interface CountryMapper extends AbstractEntityMapper<CountryDTO, Country> {

    SortedSet<CountryDTO> toDto(SortedSet<Country> countrySet);

    CountryDTO toDto(String isoCode);

    Country toEntity(String isoCode);

    default String toIsoCode(CountryDTO country) {
        if (country == null) {
            return null;
        }
        return country.getIsoCode();
    }

    default String toIsoCode(Country country) {
        if (country == null) {
            return null;
        }
        return country.getIsoCode();
    }
}
