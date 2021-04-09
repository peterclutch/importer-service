package com.peter.importerservice.service.mapper;

import com.peter.importerservice.domain.Contact;
import com.peter.importerservice.service.bo.ContactBO;
import com.peter.importerservice.service.dto.ContactCreationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    default Contact fromId(Long id) {
        if (id == null) {
            return null;
        }
        Contact contact = new Contact();
        contact.setId(id);
        return contact;
    }

    Contact toEntity(ContactCreationDTO dto);

    @Named("business")
    Contact toEntity(ContactBO bo);

    @Named("business")
    ContactBO toBO(Contact contact);

    @Named("toId")
    default Long toId(Contact entity) {
        return entity.getId();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    void updateContact(@MappingTarget Contact contact, Contact newContact);
}
