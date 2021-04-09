package com.peter.importerservice.service;

import com.peter.importerservice.domain.AdministrativeArea;
import com.peter.importerservice.exception.technical.ImportDryRunException;
import com.peter.importerservice.service.importer.configuration.Constants;
import com.peter.importerservice.service.importer.configuration.LineElement;
import com.peter.importerservice.service.importer.configuration.MultiLineElement;
import com.peter.importerservice.service.importer.dto.ImportSummaryDTO;
import com.peter.importerservice.service.importer.dto.bo.FactoryImportBO;
import com.peter.importerservice.service.importer.ImportType;
import com.peter.importerservice.service.importer.dto.FieldConfiguration;
import com.peter.importerservice.service.importer.dto.ImportConfiguration;
import com.peter.importerservice.service.importer.validation.groups.ImportGroup;
import com.peter.importerservice.service.mapper.FactoryMapper;
import com.peter.importerservice.util.BooleanUtils;
import com.peter.importerservice.util.StringUtils;
import com.peter.importerservice.validator.ValidationErrorCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

import org.simpleflatmapper.csv.CsvParser;

@Slf4j
@Service
@AllArgsConstructor
public class ImporterService {

    private final PlatformTransactionManager transactionManager;
    private final Validator validator;

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

    @Transactional(noRollbackFor = Exception.class)
    public ImportSummaryDTO importData(
            ImportType type, InputStream inputStream, ImportConfiguration configuration)
            throws IOException {

        return importData(type, inputStream, configuration, (line, constraintViolation) -> {});
    }

    @Transactional(rollbackFor = ImportDryRunException.class)
    public void simulateImportData(
            ImportType type, InputStream inputStream, ImportConfiguration configuration)
            throws IOException {

        var validationErrors = new HashMap<Long, Set<ConstraintViolation<LineElement>>>();

        var importSummary =
                importData(
                        type,
                        inputStream,
                        configuration,
                        (lineNumber, violations) -> {
                            if (!validationErrors.containsKey(lineNumber)) {
                                validationErrors.put(lineNumber, new HashSet<>());
                            }
                            validationErrors.get(lineNumber).addAll(violations);
                        });

        throw new ImportDryRunException(
                importSummary,
                manageErrors(type, configuration, validationErrors)); // force rollback on dry run
    }

    private ImportSummaryDTO importData(
            ImportType type,
            InputStream inputStream,
            ImportConfiguration configuration,
            BiConsumer<Long, Set<ConstraintViolation<LineElement>>> violationsConsumer)
            throws IOException {

        final var importSummary = new ImportSummaryDTO();
        Collections.sort(configuration.getFields());

        try (final Reader reader = new InputStreamReader(inputStream)) {
            final AtomicLong lineNumber = new AtomicLong(1);

            final var mutliLineObject = new AtomicReference<MultiLineElement>();
            final var previousObject = new AtomicReference<LineElement>();

            CsvParser.separator(configuration.getSeparator()).skip(1).stream(reader)
                    .forEach(
                            data -> {
                                var bo = this.buildBo(configuration.getFields(), type, data);
                                bo.setLineNumber(lineNumber.incrementAndGet());

                                if (type.isMultiLine()) {
                                    if (mutliLineObject.get() == null) {
                                        mutliLineObject.set(type.createMultiLineBO());
                                    }

                                    if (previousObject.get() == null) {
                                        mutliLineObject.get().addLine(bo);
                                    } else {
                                        var sameObjectAsPreviously = Objects.equals(previousObject.get(), bo);
                                        if (sameObjectAsPreviously) { // save previous objects
                                            mutliLineObject.get().addLine(bo);
                                        } else {
                                            save(
                                                    mutliLineObject.get(),
                                                    configuration,
                                                    type,
                                                    importSummary,
                                                    violationsConsumer);
                                            mutliLineObject.set(type.createMultiLineBO());
                                            mutliLineObject.get().addLine(bo);
                                        }
                                    }
                                    previousObject.set(bo);
                                } else {
                                    save(bo, configuration, type, importSummary, violationsConsumer);
                                }
                            });

            if (type.isMultiLine()) {
                save(mutliLineObject.get(), configuration, type, importSummary, violationsConsumer);
            }
        }
        return importSummary;
    }

    private boolean validate(
            LineElement bo,
            ImportSummaryDTO importSummary,
            BiConsumer<Long, Set<ConstraintViolation<LineElement>>> violationsConsumer) {

        Set<ConstraintViolation<LineElement>> constraintViolationsErrors;
        boolean isValid = true;
        if (bo instanceof MultiLineElement) {
            for (var line : ((MultiLineElement<LineElement>) bo).getLines()) {
                constraintViolationsErrors = validator.validate(line, Default.class, ImportGroup.class);
                if (!constraintViolationsErrors.isEmpty()) {
                    isValid = false;
                    violationsConsumer.accept(line.getLineNumber(), constraintViolationsErrors);
                }
            }

            constraintViolationsErrors = validator.validate(bo, Default.class, ImportGroup.class);
            if (!constraintViolationsErrors.isEmpty()) {
                isValid = false;
                for (var line : (((MultiLineElement<LineElement>) bo).getLines())) {
                    violationsConsumer.accept(line.getLineNumber(), constraintViolationsErrors);
                }
            }

            if (!isValid) {
                importSummary.incError();
            }

            return isValid;
        }

        constraintViolationsErrors = validator.validate(bo, Default.class, ImportGroup.class);
        if (!constraintViolationsErrors.isEmpty()) {
            importSummary.incError();
            violationsConsumer.accept(bo.getLineNumber(), constraintViolationsErrors);
        }

        return constraintViolationsErrors.isEmpty();
    }

    private void save(
            LineElement bo,
            ImportConfiguration configuration,
            ImportType type,
            ImportSummaryDTO importSummary,
            BiConsumer<Long, Set<ConstraintViolation<LineElement>>> violationsConsumer) {
        try {
            if (validate(bo, importSummary, violationsConsumer)) {

                TransactionTemplate txTemplate = new TransactionTemplate(transactionManager);
                txTemplate.setPropagationBehavior(
                        BooleanUtils.isTrue(configuration.getDryRun())
                                ? TransactionDefinition.PROPAGATION_REQUIRED
                                : TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                txTemplate.execute(
                        (TransactionCallback<Void>)
                                status -> {
                                    type.save(bo, ImporterService.this, configuration.getDryRun());
                                    importSummary.incSuccess();

                                    return null;
                                });
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            importSummary.incError();
        }
    }

    private <T extends LineElement> T buildBo(
            List<FieldConfiguration> fields, ImportType type, String[] data) {
        final var object = type.createBO(); // create object which represent the line content

        fields.forEach(
                field -> {
                    var element = type.getField(field.getFieldName());
                    var value = data.length >= field.getPosition() ? data[field.getPosition() - 1] : null;
                    element.ifPresent(
                            elt -> elt.setValue(object, StringUtils.isNotBlank(value) ? value : null));
                });
        return (T) object;
    }

    private Map<Long, Set<Pair<String, String>>> manageErrors(
            ImportType type,
            ImportConfiguration configuration,
            Map<Long, Set<ConstraintViolation<LineElement>>> validationErrors) {
        var allErrors = new TreeMap<Long, Set<Pair<String, String>>>();
        validationErrors
                .entrySet()
                .forEach(entry -> this.manageConstraintViolation(allErrors, type, entry, configuration));
        return allErrors;
    }

    private void manageConstraintViolation(
            Map<Long, Set<Pair<String, String>>> allErrors,
            ImportType type,
            Map.Entry<Long, Set<ConstraintViolation<LineElement>>> entry,
            ImportConfiguration configuration) {
        entry
                .getValue()
                .forEach(
                        constraintViolation -> {
                            if (manageGenericConstraintViolation(
                                    type, allErrors, entry.getKey(), constraintViolation, configuration)) {
                                return;
                            }

                            if (isDefaultMessage(constraintViolation, constraintViolation.getMessage())) {
                                // skip default textual message
                                return;
                            }

                            addError(
                                    type,
                                    allErrors,
                                    entry.getKey(),
                                    constraintViolation,
                                    constraintViolation.getMessage(),
                                    configuration);
                        });
    }

    private boolean isDefaultMessage(
            ConstraintViolation<LineElement> constraintViolation, String currentMessage) {
        var messageTemplate = constraintViolation.getMessageTemplate();
        if (messageTemplate.startsWith("{") && messageTemplate.endsWith("}")) {
            return true;
        }
        return Objects.equals(
                constraintViolation.getConstraintDescriptor().getAttributes().get("message"),
                currentMessage);
    }

    private boolean manageGenericConstraintViolation(
            ImportType type,
            Map<Long, Set<Pair<String, String>>> allErrors,
            Long lineNumber,
            ConstraintViolation<LineElement> constraintViolation,
            ImportConfiguration importConfiguration) {
        var annotation = constraintViolation.getConstraintDescriptor().getAnnotation();
        if (annotation instanceof NotNull || annotation instanceof NotBlank) {
            addError(
                    type,
                    allErrors,
                    lineNumber,
                    constraintViolation,
                    ValidationErrorCode.Common.NOT_BLANK.name(),
                    importConfiguration);
            return true;
        } else if (annotation instanceof Email) {
            addError(
                    type,
                    allErrors,
                    lineNumber,
                    constraintViolation,
                    ValidationErrorCode.Common.INVALID_EMAIL.name(),
                    importConfiguration);
        }

        return false;
    }

    private void addError(
            ImportType type,
            Map<Long, Set<Pair<String, String>>> allErrors,
            Long lineNumber,
            ConstraintViolation<LineElement> constraintViolation,
            String code,
            ImportConfiguration importConfiguration) {
        if (!allErrors.containsKey(lineNumber)) {
            allErrors.put(lineNumber, new HashSet<>());
        }
        allErrors
                .get(lineNumber)
                .add(Pair.of(getProperty(type, constraintViolation, importConfiguration), code));
    }

    private String getProperty(
            ImportType type,
            ConstraintViolation<LineElement> constraintViolation,
            ImportConfiguration importConfiguration) {
        String property = constraintViolation.getPropertyPath().toString().toUpperCase();
        if (property.contains(".")) {
            var tab = property.split("\\."); // extract field
            property = tab[tab.length - 1];
        } else if (property.indexOf('[') < property.indexOf(']')) {
            property = property.substring(property.indexOf('[') + 1, property.indexOf(']'));
        }

        if (!type.getField(property).isPresent()) {
            var opt = type.getPropertyFromCode(constraintViolation, importConfiguration.getFields());
            if (opt.isPresent()) {
                property = opt.get();
            }
        }

        var field = type.getField(property);

        return field.isPresent() ? field.get().toString() : Constants.Import.LINE_ERROR;
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
