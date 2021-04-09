package com.peter.importerservice.service;

import com.peter.importerservice.domain.AdministrativeArea;
import com.peter.importerservice.service.importer.dto.FactoryImportBO;
import com.peter.importerservice.service.importer.ImportType;
import com.peter.importerservice.service.importer.dto.FieldConfiguration;
import com.peter.importerservice.service.importer.dto.ImportConfiguration;
import com.peter.importerservice.service.mapper.FactoryMapper;
import com.peter.importerservice.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ImporterService {

    private final AdministrativeAreaService administrativeAreaService;
    private final CountryService countryService;
    private final FactoryService factoryService;

    private final FactoryMapper factoryMapper;

    public ImportConfiguration getConfiguration(ImportType type) {
        var configuration = new ImportConfiguration();
        var fields = type.getFieldsToImport();
        for (String field : fields) {
            type.getField(field)
                    .ifPresent(
                            fc -> {
                                var fieldConfiguration = new FieldConfiguration();
                                fieldConfiguration.setFieldName(fc.toString());
                                fieldConfiguration.setIsRequired(fc.isRequired());
                                configuration.addField(fieldConfiguration);
                            });
        }
        return configuration;
    }

    public void saveFactory(FactoryImportBO object, Boolean dryRun) {
        var address = object.getAddress();
        address.setCountry(countryService.getCountryByLabel(address.getCountry()).getIsoCode());
        if (StringUtils.isNotBlank(address.getAdministrativeArea())) {
            administrativeAreaService
                    .getAdministrativeAreaByLabel(address.getCountry(), address.getAdministrativeArea())
                    .map(AdministrativeArea::getIsoCode)
                    .ifPresent(address::setAdministrativeArea);
        }

        var bo = factoryMapper.toBO(object);
        factoryService.create(bo, dryRun);
    }
}
