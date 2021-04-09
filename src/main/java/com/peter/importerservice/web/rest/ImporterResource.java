package com.peter.importerservice.web.rest;

import com.peter.importerservice.exception.technical.ImportDryRunException;
import com.peter.importerservice.service.ImporterService;
import com.peter.importerservice.service.importer.ImportType;
import com.peter.importerservice.service.importer.dto.ImportConfiguration;
import com.peter.importerservice.service.importer.dto.ImportReportDTO;
import com.peter.importerservice.service.importer.dto.ImportSummaryDTO;
import com.peter.importerservice.validator.AllowedMimeType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/import")
@Validated
public class ImporterResource {

    private final ImporterService importerService;

    @GetMapping("/{type}/configuration")
    public ResponseEntity<ImportConfiguration> getConfiguration(
            @PathVariable("type") ImportType type) {

        managePermissions(type);
        var configuration = this.importerService.getConfiguration(type);
        return ResponseEntity.ok(configuration);
    }

    @PostMapping("/{type}")
    public ResponseEntity<ImportReportDTO> importFile(
            @PathVariable("type") ImportType type,
            @Valid @RequestPart("configuration") ImportConfiguration configuration,
            @AllowedMimeType({"text/csv", "application/vnd.ms-excel"}) @RequestPart("file")
                    MultipartFile file)
            throws IOException {

        managePermissions(type);
        ImportSummaryDTO summaryDTO = null;

        try (var inputStream = file.getInputStream()) {
            if (configuration.getDryRun()) {
                this.importerService.simulateImportData(type, inputStream, configuration);
            } else {
                summaryDTO = this.importerService.importData(type, inputStream, configuration);
            }
        } catch (ImportDryRunException e) {
            return ResponseEntity.ok(
                    new ImportReportDTO().summary(e.getImportSummary()).errors(e.getErrors()));
        }

        return ResponseEntity.ok(new ImportReportDTO().summary(summaryDTO));
    }

    private void managePermissions(ImportType type) {
//        boolean permissionMissing =
//                type.getPermissions().stream()
//                        .map(SecurityUtils::hasCurrentUserPermission)
//                        .anyMatch(p -> p == false);
//        if (permissionMissing) {
//            // we assume the exception is an invalidInputException
//            // because it's depend to the import TYPE
//            throw new InvalidInputException(ErrorLabel.OPERATION_NOT_PERMITTED.getMessage());
//        }
    }
}
