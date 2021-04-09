package com.peter.importerservice.web.rest;

import com.peter.importerservice.service.ImporterService;
import com.peter.importerservice.service.importer.ImportType;
import com.peter.importerservice.service.importer.dto.ImportConfiguration;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

//        managePermissions(type);
        var configuration = this.importerService.getConfiguration(type);
        return ResponseEntity.ok(configuration);
    }
}
