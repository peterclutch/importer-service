package com.peter.importerservice.cucumber.stepdefs;

import com.peter.importerservice.cucumber.context.ScenarioContextKeys;
import com.peter.importerservice.cucumber.context.World;
import com.peter.importerservice.cucumber.web.rest.QimaTestUtil;
import com.peter.importerservice.repository.FactoryRepository;
import com.peter.importerservice.service.importer.ImportType;
import com.peter.importerservice.service.importer.dto.FieldConfiguration;
import com.peter.importerservice.service.importer.dto.ImportConfiguration;
import com.peter.importerservice.service.importer.dto.ImportReportDTO;
import cucumber.api.java8.En;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ImportStepDefsClean implements En {

    private final World world;
    private final FactoryRepository factoryRepository;

    public ImportStepDefsClean(World world, FactoryRepository factoryRepository) {
        this.world = world;
        this.factoryRepository = factoryRepository;

        Then(
                "I get {ImportType} configuration",
                (ImportType importType) -> {
                    var configuration =
                            this.world
                                    .getTestRestTemplate()
                                    .getForEntity(
                                            world.getBaseUrl() + "/api/import/" + importType + "/configuration",
                                            ImportConfiguration.class);

                    world.set(ScenarioContextKeys.CONFIGURATION_RESULT, configuration);
                });

        And(
                "The {ImportType} configuration is correct",
                (ImportType importType) -> {
                    var fields = new ArrayList<>(importType.getFieldsToImport());

                    var configuration =
                            (ResponseEntity<ImportConfiguration>)
                                    world.get(ScenarioContextKeys.CONFIGURATION_RESULT);
                    assertThat(configuration.getStatusCode()).isEqualTo(HttpStatus.OK);
                    assertThat(fields.size()).isEqualTo(configuration.getBody().getFields().size());

                    var responseFields =
                            configuration.getBody().getFields().stream()
                                    .map(FieldConfiguration::getFieldName)
                                    .collect(Collectors.toList());
                    fields.removeIf(c -> responseFields.contains(c));
                    assertThat(fields).isEmpty();
                });

        Then(
                "I try a {ImportType} import with dry run option {string}",
                (ImportType importType, String dryRun) -> {
                    ImportConfiguration configuration = getConfiguration(importType.name());
                    configuration.setDryRun(Boolean.valueOf(dryRun));

                    MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
                    HttpHeaders csvHeaders = new HttpHeaders();
                    csvHeaders.add("content-type", "text/csv");

                    HttpEntity partRequest =
                            new HttpEntity(
                                    new FileSystemResource(
//                                    new ClassPathResource(
                                            "C:/Source/importer-service/src/test/resources/com.peter.importerservice/cucumber.stepdefs/import/import_"
                                                    + importType.name().toLowerCase()
                                                    + ".csv"),
                                    csvHeaders);
                    parts.add("file", partRequest);

                    HttpHeaders dataHeader = new HttpHeaders();
                    dataHeader.setContentType(MediaType.APPLICATION_JSON);
                    parts.add("configuration", new HttpEntity<>(configuration, dataHeader));

                    HttpHeaders requestHeaders = new HttpHeaders();
                    requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

                    HttpEntity entityRequest = new HttpEntity<>(parts, requestHeaders);

                    var simulatedImport =
                            this.world
                                    .getTestRestTemplate()
                                    .exchange(
                                            world.getBaseUrl() + "/api/import/" + importType,
                                            HttpMethod.POST,
                                            entityRequest,
                                            ImportReportDTO.class);

                    world.set(ScenarioContextKeys.DRY_RUN_RESULT, simulatedImport);
                });

        And(
                "The file contains {string} errors",
                (String errorCpt) -> {
                    var result =
                            (ResponseEntity<ImportReportDTO>) world.get(ScenarioContextKeys.DRY_RUN_RESULT);
                    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
                    assertThat(result.getBody().getErrors().size()).isEqualTo(Integer.valueOf(errorCpt));
                    assertThat(result.getBody().getSummary().getError()).isEqualTo(Long.valueOf(errorCpt));
                });

        And(
                "The line {string} is valid",
                (String lineNumber) -> {
                    var result =
                            (ResponseEntity<ImportReportDTO>) world.get(ScenarioContextKeys.DRY_RUN_RESULT);
                    var isLineOnError =
                            result.getBody().getErrors().stream()
                                    .anyMatch(line -> line.getLineNumber().equals(Long.valueOf(lineNumber)));
                    assertThat(isLineOnError).isFalse();
                });

        And(
                "The line {string} is in error with code {string} on field {string}",
                (String lineNumber, String code, String field) -> {
                    var result =
                            (ResponseEntity<ImportReportDTO>) world.get(ScenarioContextKeys.DRY_RUN_RESULT);
                    var lineResult =
                            result.getBody().getErrors().stream()
                                    .filter(line -> line.getLineNumber().equals(Long.valueOf(lineNumber)))
                                    .flatMap(line -> line.getErrors().stream())
                                    .filter(fieldError -> fieldError.getFieldName().equals(field))
                                    .filter(fieldError -> fieldError.getError().equals(code))
                                    .findFirst();
                    assertThat(lineResult.isPresent()).isTrue();
                });

        And(
                "The {ImportType} in line {string} is not saved",
                (ImportType importType, String lineNumber) -> {
                    var importTypeName = importType.name();
                    if (importTypeName.equalsIgnoreCase("product")) {
                        // TODO
//            var identifier = getIdentifierValue(importTypeName, Integer.valueOf(lineNumber));
//
//            var found =
//                productRepository.existsByBrandIdAndIdentifierValue(
//                    QimaTestUtil.TEST_BRAND_ID, identifier);
//            assertThat(found).isFalse();

                    } else if (importTypeName.equalsIgnoreCase("user")) {
                        // TODO
//            var email = getUserEmail(importTypeName, Integer.valueOf(lineNumber));
//            var found = userRepository.findOneByContactEmailIgnoreCase(email).isPresent();
//            assertThat(found).isFalse();
                    } else if (importTypeName.equalsIgnoreCase("purchase_order")) {
                        // TODO
//            var purchaseOrder =
//                getPurchaseOrderReference(importTypeName, Integer.valueOf(lineNumber));
//            var found =
//                purchaseOrderRepository.existsByReferenceAndBrandId(
//                    purchaseOrder, QimaTestUtil.TEST_BRAND_ID);
//            assertThat(found).isFalse();
                    } else if (importTypeName.equalsIgnoreCase("defect_checklist")) {
                        // TODO
//            var defectChecklist =
//                getDefectsChecklistName(importTypeName, Integer.valueOf(lineNumber));
//            var found =
//                defectsChecklistRepository.existsByNameAndBrandId(
//                    defectChecklist, QimaTestUtil.TEST_BRAND_ID);
//            assertThat(found).isFalse();
                    } else if (importTypeName.equalsIgnoreCase("test_checklist")) {
                        // TODO
//            var testChecklist = getTestsChecklistName(importTypeName, Integer.valueOf(lineNumber));
//            var found =
//                testsChecklistRepository.existsByNameAndBrandId(
//                    testChecklist, QimaTestUtil.TEST_BRAND_ID);
//            assertThat(found).isFalse();
                    } else {
                        throw new IllegalArgumentException();
                    }
                });

        And(
                "The {ImportType} in line {string} is saved",
                (ImportType importType, String lineNumber) -> {
                    var importTypeName = importType.name();
                    if (importTypeName.equalsIgnoreCase(ImportType.PRODUCT.name())) {
                        // TODO
//            var identifier = getIdentifierValue(importTypeName, Integer.valueOf(lineNumber));
//            var found =
//                productRepository.existsByBrandIdAndIdentifierValue(
//                    QimaTestUtil.TEST_BRAND_ID, identifier);
//            assertThat(found).isTrue();
                    } else if (importTypeName.equalsIgnoreCase(ImportType.USER.name())) {
                        // TODO
//            var email = getUserEmail(importTypeName, Integer.valueOf(lineNumber));
//            var found = userRepository.findOneByContactEmailIgnoreCase(email).isPresent();
//            assertThat(found).isTrue();
                    } else if (importTypeName.equalsIgnoreCase(ImportType.TEST_CHECKLIST.name())) {
                        // TODO
//            var name = getTestsChecklistName(importTypeName, Integer.valueOf(lineNumber));
//            var found =
//                testsChecklistRepository.existsByNameAndBrandId(name, QimaTestUtil.TEST_BRAND_ID);
//            assertThat(found).isTrue();
                    } else if (importTypeName.equalsIgnoreCase(ImportType.FACTORY.name())) {
                        var name = getFactoryName(importTypeName, Integer.valueOf(lineNumber));
                        var found = factoryRepository.existsByNameAndBrandId(name, QimaTestUtil.TEST_BRAND_ID);
                        assertThat(found).isTrue();
                    } else {
                        throw new IllegalArgumentException();
                    }
                });
    }

    private String getFactoryName(String importType, Integer line) {
        var currentLine = getLine(importType, line);
        var config = getConfiguration(importType);

        return config.getFields().stream()
                .filter(f -> f.getFieldName().equalsIgnoreCase("factory_name"))
                .map(f -> currentLine.split(";")[f.getPosition() - 1])
                .findFirst()
                .orElseThrow();
    }

    private String getIdentifierValue(String importType, Integer line) {
        return getLine(importType, line).split(";")[0];
    }

    private String getUserEmail(String importType, Integer line) {
        var currentLine = getLine(importType, line);
        var config = getConfiguration(importType);

        return config.getFields().stream()
                .filter(f -> f.getFieldName().equalsIgnoreCase("email"))
                .map(f -> currentLine.split(";")[f.getPosition() - 1])
                .findFirst()
                .orElseThrow();
    }

    // TODO
//  private String getPurchaseOrderReference(String importType, Integer line) {
//    var currentLine = getLine(importType, line);
//    var config = getConfiguration(importType);
//
//    return config.getFields().stream()
//        .filter(f -> f.getFieldName().equalsIgnoreCase(ImportPurchaseOrderFields.REFERENCE.name()))
//        .map(f -> currentLine.split(";")[f.getPosition() - 1])
//        .findFirst()
//        .orElseThrow();
//  }

    // TODO
//  private String getDefectsChecklistName(String importType, Integer line) {
//    var currentLine = getLine(importType, line);
//    var config = getConfiguration(importType);
//
//    return config.getFields().stream()
//        .filter(
//            f ->
//                f.getFieldName()
//                    .equalsIgnoreCase(ImportDefectsChecklistFields.DEFECT_CHECKLIST_NAME.name()))
//        .map(f -> currentLine.split(";")[f.getPosition() - 1])
//        .findFirst()
//        .orElseThrow();
//  }

    // TODO
//  private String getTestsChecklistName(String importType, Integer line) {
//    var currentLine = getLine(importType, line);
//    var config = getConfiguration(importType);
//
//    return config.getFields().stream()
//        .filter(
//            f ->
//                f.getFieldName()
//                    .equalsIgnoreCase(ImportTestsChecklistFields.TEST_CHECKLIST_NAME.name()))
//        .map(f -> currentLine.split(";")[f.getPosition() - 1])
//        .findFirst()
//        .orElseThrow();
//  }

    private String getLine(String type, Integer line) {
        Scanner scanner;
        try {
            scanner =
                    new Scanner(
                            new FileSystemResource(
//              new ClassPathResource(
                                    "C:/Source/importer-service/src/test/resources/com.peter.importerservice/cucumber.stepdefs/import/import_"
                                            + type.toLowerCase()
                                            + ".csv")
                                    .getInputStream());

            int currentLineNumber = 0;
            String currentLine;
            do {
                currentLine = scanner.nextLine();
                currentLineNumber++;
            } while (currentLineNumber != line);

            return currentLine;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private ImportConfiguration getConfiguration(String type) {
        try {
            return QimaTestUtil.mapper.readValue(
                    new FileSystemResource(
//          new ClassPathResource(
                            "C:/Source/importer-service/src/test/resources/com.peter.importerservice/cucumber.stepdefs/import/configuration_import_"
                                    + type.toLowerCase()
                                    + ".json")
                            .getInputStream(),
                    ImportConfiguration.class);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
