package com.peter.importerservice.service.importer;

import com.peter.importerservice.service.ImporterService;
import com.peter.importerservice.service.importer.configuration.ImportConfiguration;
import com.peter.importerservice.service.importer.configuration.ImportConfigurationFields;
import com.peter.importerservice.service.importer.configuration.LineElement;
import com.peter.importerservice.service.importer.configuration.MultiLineConfiguration;
import com.peter.importerservice.service.importer.configuration.MultiLineElement;
import com.peter.importerservice.service.importer.dto.FactoryImportBO;
import com.peter.importerservice.service.importer.configuration.factory.ImportFactoryConfiguration;
import com.peter.importerservice.service.importer.dto.FieldConfiguration;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.TriConsumer;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public enum ImportType {
//    PRODUCT(
//            new ImportProductConfiguration(),
//            (object, service, dryRun) -> service.saveProduct((ProductImporterBO) object, dryRun)),
//    USER(
//            new ImportUserConfiguration(),
//            (object, service, dryRun) -> service.saveUser((UserImportBO) object, dryRun)),
//    PURCHASE_ORDER(
//            new ImportPurchaseOrderConfiguration(),
//            (object, service, dryRun) ->
//                    service.savePurchaseOrder((PurchaseOrderImportBO) object, dryRun)),
//    DEFECT_CHECKLIST(
//            new ImportDefectsChecklistConfiguration(),
//            (object, service, dryRun) ->
//                    service.saveDefectsChecklist((DefectsChecklistImportBO) object, dryRun)),
//    TEST_CHECKLIST(
//            new ImportTestsChecklistConfiguration(),
//            (object, service, dryRun) ->
//                    service.saveTestsChecklist((TestsChecklistImportBO) object, dryRun)),
    FACTORY(
            new ImportFactoryConfiguration(),
            (object, service, dryRun) -> service.saveFactory((FactoryImportBO) object, dryRun));

    private final ImportConfiguration configuration;
    private final TriConsumer<Object, ImporterService, Boolean> saveConsumer;

    public List<String> getFieldsToImport() {
        return Stream.of(this.configuration.getFieldsEnum().getEnumConstants())
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    public <T extends LineElement> T createBO() {
        return (T) this.configuration.createBO();
    }

    public <T extends MultiLineElement> T createMultiLineBO() {
        if (this.configuration.isMultiLine()) {
            return (T) ((MultiLineConfiguration) this.configuration).createMultiLineBO();
        }
        throw new RuntimeException(
                "No multiline BO available for this configuration", new Exception());
    }

    public <T extends ImportConfigurationFields> Optional<T> getField(String name) {
        return (Optional)
                Stream.of(this.configuration.getFieldsEnum().getEnumConstants())
                        .filter(
                                e -> e.toString().replaceAll("_", "").equalsIgnoreCase(name.replaceAll("_", "")))
                        .findFirst();
    }

    public void save(Object object, ImporterService service, Boolean dryRun) {
        saveConsumer.accept(object, service, dryRun);
    }

    public FieldConfiguration getField(List<FieldConfiguration> configurations, Integer position) {
        return configurations.stream()
                .filter(field -> field.getPosition().equals(position))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public Set<String> getPermissions() {
        return configuration.getPermissions();
    }

    public boolean isValid(List<FieldConfiguration> value, ConstraintValidatorContext context) {
        return this.configuration.isValid(value, context);
    }

    public Optional<String> getPropertyFromCode(
            ConstraintViolation<LineElement> code, List<FieldConfiguration> configuration) {
        return this.configuration.convertFieldName(code, configuration);
    }

    public boolean isMultiLine() {
        return this.configuration.isMultiLine();
    }
}
