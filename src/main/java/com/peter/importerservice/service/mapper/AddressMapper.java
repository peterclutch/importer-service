package com.peter.importerservice.service.mapper;

import com.peter.importerservice.domain.Address;
import com.peter.importerservice.service.bo.AddressBO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
        componentModel = "spring",
        uses = {
                AdministrativeAreaMapper.class,
                CountryMapper.class
        },
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AddressMapper {

    default Address fromId(Long id) {
        if (id == null) {
            return null;
        }
        Address address = new Address();
        address.setId(id);
        return address;
    }

    @Mapping(source = "administrativeArea", target = "areaCode")
    Address toEntity(AddressBO bo);

    @Named("business")
    @Mapping(source = "areaCode", target = "administrativeArea")
    AddressBO toBO(Address address);
}
