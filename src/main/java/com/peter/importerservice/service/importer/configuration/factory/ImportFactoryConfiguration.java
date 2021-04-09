package com.peter.importerservice.service.importer.configuration.factory;

import com.peter.importerservice.service.importer.configuration.ImportConfiguration;
import com.peter.importerservice.service.importer.dto.FactoryImportBO;
import com.peter.importerservice.service.importer.dto.FieldConfiguration;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ImportFactoryConfiguration implements ImportConfiguration<FactoryImportBO> {

    private static final Set<String> PERMISSIONS = Set.of("FACTORY_EDIT");

    @Override
    public Class getFieldsEnum() {
        return ImportFactoryFields.class;
    }

    @Override
    public Set<String> getPermissions() {
        return PERMISSIONS;
    }

    @Override
    public Optional<String> convertFieldName(
            ConstraintViolation<Object> code, List<FieldConfiguration> configuration) {
        String result = null;

        var prop = code.getPropertyPath() == null ? "" : code.getPropertyPath().toString();

        if ("address.street".equalsIgnoreCase(prop)) {
            result = ImportFactoryFields.ADDRESS_DETAILS.name();
        } else if ("contact.lastName".equalsIgnoreCase(prop)) {
            result = ImportFactoryFields.CONTACT_LAST_NAME.name();
        } else if ("contact.firstName".equalsIgnoreCase(prop)) {
            result = ImportFactoryFields.CONTACT_FIRST_NAME.name();
        } else if ("contact.localName".equalsIgnoreCase(prop)) {
            result = ImportFactoryFields.CONTACT_LOCAL_NAME.name();
        } else if ("name".equalsIgnoreCase(prop)) {
            result = ImportFactoryFields.FACTORY_NAME.name();
        } else if ("localName".equalsIgnoreCase(prop)) {
            result = ImportFactoryFields.FACTORY_LOCAL_NAME.name();
        } else if ("contact.phone".equalsIgnoreCase(prop)) {
            result = ImportFactoryFields.WORK_PHONE.name();
        }
        return Optional.ofNullable(result);
    }
}
